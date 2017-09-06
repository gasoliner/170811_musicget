package cn.musicget;

import cn.musicget.pageProcessor.KwPageProcessor;
import cn.musicget.pipeLine.KwPipeLine;
import cn.musicget.po.ErrorSong;
import cn.musicget.util.PageUtil;
import com.alibaba.fastjson.JSON;
import us.codecraft.webmagic.Spider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by 万洪基 on 2017/8/12.
 */
public class Main {

    public static void main(String[] args) throws Exception {

        List<ErrorSong> errorSongList = new ArrayList<ErrorSong>();
        KwPageProcessor processor = new KwPageProcessor(6748);
        KwPipeLine pipeLine = new KwPipeLine(errorSongList);

        Spider.create(processor)
                .addPipeline(pipeLine)
                .thread(5)
                .addUrl(
                        "http://www.kuwo.cn/artist/indexAjax?category=0&prefix=&pn=0"
//                        "http://www.kuwo.cn/yinyue/6468891"
                )
                .run();


//        保存到文件JSON
        PageUtil.toFile(JSON.toJSONString(errorSongList),"F:\\errorSongList.json");

    }
}
