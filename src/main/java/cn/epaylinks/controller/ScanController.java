package cn.epaylinks.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import cn.epaylinks.common.ScanPool;
import cn.epaylinks.common.PoolCache;

@Controller
@EnableAutoConfiguration
public class ScanController {

    @RequestMapping("/qrcode/{uuid}")
    @ResponseBody
    String createQRCode(@PathVariable String uuid, HttpServletResponse response) {
        System.out.println("生成二维码");

        String text = "http://94a9a217.ngrok.io/login/" + uuid;
        int width = 300;
        int height = 300;
        String format = "png";
        //将UUID放入缓存
        ScanPool pool = new ScanPool();
        PoolCache.cacheMap.put(uuid, pool);
        try {
            Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //hints.put(EncodeHintType.MARGIN, 1);
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); //容错率
            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
            MatrixToImageWriter.writeToStream(bitMatrix, format, response.getOutputStream());
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/login/{uuid}")
    @ResponseBody
    String login(@PathVariable String uuid) {

        ScanPool pool = PoolCache.cacheMap.get(uuid);

        if (pool == null) {
            return "timeout,scan fail";
        }

        pool.scanSuccess();

        return "扫码完成，登录成功";
    }

    @RequestMapping("/pool")
    @ResponseBody
    String pool(String uuid) {
        System.out.println("检测[" + uuid + "]是否登录");

        ScanPool pool = PoolCache.cacheMap.get(uuid);

        if (pool == null) {
            return "timeout";
        }

        //使用计时器，固定时间后不再等待扫描结果--防止页面访问超时
        new Thread(new ScanCounter(uuid)).start();

        boolean scanFlag = pool.getScanStatus();
        if (scanFlag) {
            return "success";
        } else {
            return "fail";
        }
    }

    class ScanCounter implements Runnable {

        public Long timeout = 27000L;

        //传入的对象
        private String uuid;

        public ScanCounter(String p) {
            uuid = p;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            notifyPool(uuid);
        }

        public synchronized void notifyPool(String uuid) {
            ScanPool pool = PoolCache.cacheMap.get(uuid);
            pool.notifyPool();
        }
    }
}