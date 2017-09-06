package cn.musicget.po;

public class Song {
    private Long id;

    private String name;

    private String album;

    private String singer;

    private String lyric;

    private Long kwid;

    private Integer comments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album == null ? null : album.trim();
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer == null ? null : singer.trim();
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric == null ? null : lyric.trim();
    }

    public Long getKwid() {
        return kwid;
    }

    public void setKwid(Long kwid) {
        this.kwid = kwid;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {

        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", album='" + album + '\'' +
                ", singer='" + singer + '\'' +
                ", lyric='" + lyric + '\'' +
                ", kwid=" + kwid +
                ", comments=" + comments +
                '}';
    }
}