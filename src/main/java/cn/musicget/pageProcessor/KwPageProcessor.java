package cn.musicget.pageProcessor;

import cn.musicget.po.Song;
import cn.musicget.util.PageUtil;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.JsonPathSelector;

import java.io.IOException;
import java.util.*;

/**
 * Created by 万洪基 on 2017/8/12.
 */
public class KwPageProcessor implements PageProcessor {

    private ArrayList<String> userAgents;
    private long countURL;
    private long countSong;
    private int currSingerListPage;
    private int allSingerListPage;
    private long currentSingerId;
    private String currentInfo;


    public KwPageProcessor(int allpage) {
        currentSingerId = 0;
        allSingerListPage = allpage;
        currSingerListPage = 0;
        countURL = 0;
        countSong = 0;
        try {
            userAgents = PageUtil.getUserAgentList("F:\\userAgents.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//     Domain
    private String DOMAIN = "http://www\\.kuwo\\.cn";
//    歌手列表
    private String SINGER_LIST_VIEW = DOMAIN + "/artist/indexAjax.*";
//    歌曲列表
    private String SONG_LIST_VIEW = DOMAIN + "/artist/contentMusicsAjax\\?artistId=\\d+&pn=\\d*&rn=\\d*";
//    歌曲详情
    private String SONG_VIEW = DOMAIN + "/yinyue/\\d+";
//    歌手首页
    private String SINGER_INDEX_VIEW = DOMAIN + "/artist/content\\?name=.+";


    private Site site =
            Site.me().addHeader("Referer",DOMAIN)
                    .addHeader("Cookie",
                            ""
                    )
                    .setDomain(DOMAIN)
                    .setRetryTimes(1)
                    .setSleepTime(1)
                    .setUserAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");


    private boolean flag1 = true;

    public void process(Page page) {

        /***
         * 如果是歌手列表页面
         * http://www.kuwo.cn/artist/indexAjax?category=0&prefix=&pn=0
         * info：
         * SINGER_INDEX_VIEW（歌手首页）
         */
        if (page.getUrl().regex(SINGER_LIST_VIEW).match()) {
//            System.out.println("如果是歌手列表页面");
            page.addTargetRequests(page.getHtml().links().regex(SINGER_INDEX_VIEW).all());
            if (flag1){
                List<String> urlList = new LinkedList<String>();
                for (int i = 0; i < allSingerListPage; i++) {
                    urlList.add("http://www.kuwo.cn/artist/indexAjax?category=0&prefix=&pn="+i);
                }
                page.addTargetRequests(urlList);
                flag1 = false;
            }
            currentInfo = "kwSpider/歌手列表页面/";

        }
        /***
         * 如果是歌手首页
         * http://www.kuwo.cn/artist/content?name=%E8%B5%B5%E9%9B%B7
         * info：
         * <div class="page" data-page="12"></div>
         * 歌曲列表总数
         * 歌手artistId
         */
        if (page.getUrl().regex(SINGER_INDEX_VIEW).match()) {
//            System.out.println("如果是歌手首页");
            String listCount = page.getHtml().xpath("//div[@class='page']/outerHtml()").get();
            listCount = PageUtil.getValueByKeyInHtml(listCount,"data-page");
            currentSingerId = Long.parseLong(PageUtil.getValueByKeyInHtml(
                    page.getHtml().xpath("//div[@class='artistTop']/outerHtml()").get(),
                    "data-artistid"
            ));
            System.out.println("currentSingerId:\t"+currentSingerId);
            List<String> urlList = new LinkedList<String>();
            for (int i = 0; i < Integer.parseInt(listCount); i++) {
                urlList.add("http://www.kuwo.cn/artist/contentMusicsAjax?artistId=" + currentSingerId + "&pn=" + i + "&rn=15");
            }
            page.addTargetRequests(urlList);
            currentInfo = "kwSpider/歌手首页页面/currentSingerId = " + currentSingerId + "/listCount = " + listCount;
        }

        /***
         * 如果是歌曲列表
         * http://www.kuwo.cn/artist/contentMusicsAjax?artistId=125658&pn=2&rn=15
         * info:
         * SONG_VIEW（歌曲详情）
         */
        if (page.getUrl().regex(SONG_LIST_VIEW).match()) {
//            System.out.println("如果是歌曲列表");
            page.addTargetRequests(page.getHtml().links().regex(SONG_VIEW).all());
            currentInfo = "kwSpider/歌曲列表页面/";
        }

        /***
         * 如果是歌曲详情
         * http://www.kuwo.cn/yinyue/6468891
         * info：
         * 歌曲
         *      |-name
         *      |-singer
         *      |-album
         *      |-lyric
         *      |-kwid
         *      |-comments
         */
        if (page.getUrl().regex(SONG_VIEW).match()) {
//            System.out.println("如果是歌曲详情页面");
            Song song = new Song();
            song.setName(page.getHtml().xpath("//p[@id='lrcName']/text()").get());
            song.setSinger(page.getHtml().xpath("//p[@class='artist']/span/a/text()").get());
            song.setAlbum(page.getHtml().xpath("//p[@class='album']/span/a/text()").get());
            song.setLyric(page.getHtml().xpath("//div[@id='lrcContent']/tidyText()").get());
            song.setKwid(Long.valueOf(PageUtil.getRequestName(page.getUrl().toString())));
//            http://comment.kuwo.cn/com.s?type=get_comment&uid=0&digest=15&sid=6468891&page=1&rows=20&f=web
//            设置JsonPath获取comments
            JsonPathSelector selector = new JsonPathSelector("$.total");
            try {
                String comments = selector.select(PageUtil.sendRequest(
                        "http://comment.kuwo.cn/com.s?type=get_comment&uid=0&digest=15&sid=" + song.getKwid() + "&page=1&rows=20&f=web"
                ));
                song.setComments(Integer.parseInt(comments));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (song != null){
                page.putField("song",song);
            }else {
                page.setSkip(true);
            }
//            System.out.println("song:\t"+song);
            currentInfo = "kwSpider/歌曲详情页面/songName = " + song.getName() + "/singer = " + song.getSinger() +"/album = " + song.getAlbum() + "/kwid = " + song.getKwid() + "/comments = " + song.getComments();
        }
//        必要的时候打开这句话
//        PageUtil.stopRandomTime(1,1500);
        site.setUserAgent(randomUserAgent());
        currentInfo = PageUtil.dateToString(new Date(System.currentTimeMillis())) + ":\t" + currentInfo;
        System.out.println("currentInfo:\t" + currentInfo);

        if (System.currentTimeMillis()%20000 >= 0 || System.currentTimeMillis()%20000 <=1500 ) {
            try {
                PageUtil.toFile(currentInfo,"F:\\pageProcessorInfo");
            } catch (IOException e) {
            }
        }

    }

    public Site getSite() {
        return site;
    }

    public String randomUserAgent(){
        Random random = new Random();
        return userAgents.get(random.nextInt(userAgents.size()-1));
    }

    public String getCurrentInfo() {
        return currentInfo;
    }
}
