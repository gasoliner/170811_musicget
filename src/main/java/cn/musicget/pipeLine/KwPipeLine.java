package cn.musicget.pipeLine;

import cn.musicget.mapper.SongMapper;
import cn.musicget.po.ErrorSong;
import cn.musicget.po.Song;
import cn.musicget.util.PageUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by 万洪基 on 2017/8/13.
 */
public class KwPipeLine implements Pipeline {

    private List<ErrorSong> errorSongList;
    private int objectCount;
    ApplicationContext applicationContext;
    SongMapper songMapper;

    public KwPipeLine(List<ErrorSong> errorSongList) {
        this.errorSongList = errorSongList;
        applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
        songMapper = (SongMapper) applicationContext.getBean("songMapper");
        objectCount = 0;
    }

    public void process(ResultItems resultItems, Task task) {
        if (resultItems.get("song") != null){
            Song song = resultItems.get("song");
            try {
                songMapper.insertSelective(song);
                objectCount++;
            } catch (Exception e) {
                e.fillInStackTrace();
                System.out.println("插入出错了！！");
                ErrorSong errorSong = new ErrorSong();
                if (song.getSinger() != null){
                    errorSong.setSinger(song.getSinger());
                }
                errorSong.setErrorTime(new Date(System.currentTimeMillis()));
                if (song.getKwid() != null){
                    errorSong.setKwId(song.getKwid());
                }
                if (song.getName() != null){
                    errorSong.setSongName(song.getName());
                }
                if (song.getLyric() != null){
                    errorSong.setLyric(song.getLyric());
                }
                errorSongList.add(errorSong);
            }
            resultItems.put("song",null);
        }

        if (System.currentTimeMillis()%20000 >= 0 || System.currentTimeMillis()%20000 <=2000 ) {
            try {
                PageUtil.toFile(getPipeLineCurrentInfo(),"F:\\pipeLineInfo");
            } catch (IOException e) {
            }
        }
    }

    public List<ErrorSong> getErrorSongList() {
        return errorSongList;
    }

    public String getPipeLineCurrentInfo() {
        return PageUtil.dateToString(new Date(System.currentTimeMillis())) + ":\t" + "KwSpider/objectCount = " + objectCount;
    }

    public int getObjectCount() {
        return objectCount;
    }
}
