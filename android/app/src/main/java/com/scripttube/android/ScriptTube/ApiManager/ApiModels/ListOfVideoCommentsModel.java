package com.scripttube.android.ScriptTube.ApiManager.ApiModels;

import java.util.List;

public class ListOfVideoCommentsModel {
    public String videoId;
    public String limit;
    public String page;
    public int status;
    public String message;
    public List<Data> data;
    public ListOfVideoCommentsModel(String videoId, String limit, String page) {
        this.videoId = videoId;
        this.limit = limit;
        this.page = page;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<Data> getData() {
        return data;
    }

    public class Data {
        public String _id;
        public String comment;
        public String videoId;
        public List<ReplyId> replyId;
        public CommentBy commentBy;
        public String createdAt;
        public String updatedAt;
        public int __v;

        public CommentBy getCommentBy() {
            return commentBy;
        }

        public int get__v() {
            return __v;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public String get_id() {
            return _id;
        }

        public String getComment() {
            return comment;
        }

        public String getVideoId() {
            return videoId;
        }

        public List<ReplyId> getReplyId() {
            return replyId;
        }

        public class ReplyId {
            public String _id;
            public String reply;
            public ReplyBy replyBy;

            public String get_id() {
                return _id;
            }

            public String getReply() {
                return reply;
            }

            public ReplyBy getReplyBy() {
                return replyBy;
            }

            public class ReplyBy {
                public String _id;
                public String userName;
                public String profileImage;

                public String get_id() {
                    return _id;
                }

                public String getUserName() {
                    return userName;
                }

                public String getProfileImage() {
                    return profileImage;
                }
            }
        }

        public class CommentBy {
            public String _id;
            public String userName;
            public String profileImage;

            public String get_id() {
                return _id;
            }

            public String getUserName() {
                return userName;
            }

            public String getProfileImage() {
                return profileImage;
            }
        }
    }
}
