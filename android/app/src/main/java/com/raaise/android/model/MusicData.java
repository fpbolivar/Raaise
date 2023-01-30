package com.raaise.android.model;

public class MusicData {
    public String _id;
    public String songName;
    public String artistName;
    public String Thumbnail;
    public String audio;
    public String audioTime;
    public GenreID genreId;

    public boolean isPlaying = false;

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public String getId() {
        return _id;
    }

    public String getSongName() {
        return songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getThumbnail() {
        return Thumbnail;
    }

    public String getAudio() {
        return audio;
    }

    public String getAudioTime() {
        return audioTime;
    }

    public GenreID getGenreId() {
        return genreId;
    }
}
