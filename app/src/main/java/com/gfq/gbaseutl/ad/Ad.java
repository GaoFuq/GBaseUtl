package com.gfq.gbaseutl.ad;

public class Ad {

    /**
     * contentType :
     * conversionForm :
     * createTime :
     * id : 0
     * name :
     * note :
     * platform : {"createTime":"","endWordFour":"","endWordOne":"","endWordThree":"","endWordTwo":"","id":0,"isAndroid":0,"isQuick":0,"name":"","password":"","phone":"","platformType":"","presenceStatus":0,"reservationPath":"","startWordFour":"","startWordOne":"","startWordThree":"","startWordTwo":"","status":0,"updateTime":"","username":"","wechatPath":"","wechatQc":""}
     * platformId : 0
     * playSpeed :
     * presenceStatus : 0
     * updateTime :
     * url :
     */

    private String contentType;//内容类型,可用值:IMAGE,VIDEO,NOTIFICATION
    private String conversionForm;//转换形式（图片）,可用值:FlipVerticalTransformer,FlipHorizontalTransformer,ZoomInTransformer
    private String createTime;
    private int id;
    private String name;//标题（前端没卵用，后台用的字段）
    private String note;//备注(通知的内容)
    private PlatformBean platform;
    private int platformId;
    private String playSpeed;//播放速度(通知),可用值:QUICK,MEDIUM,SLOW
    private int presenceStatus;//0-删除 1-新建
    private String updateTime;
    private String url;//内容地址

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getConversionForm() {
        return conversionForm;
    }

    public void setConversionForm(String conversionForm) {
        this.conversionForm = conversionForm;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public PlatformBean getPlatform() {
        return platform;
    }

    public void setPlatform(PlatformBean platform) {
        this.platform = platform;
    }

    public int getPlatformId() {
        return platformId;
    }

    public void setPlatformId(int platformId) {
        this.platformId = platformId;
    }

    public String getPlaySpeed() {
        return playSpeed;
    }

    public void setPlaySpeed(String playSpeed) {
        this.playSpeed = playSpeed;
    }

    public int getPresenceStatus() {
        return presenceStatus;
    }

    public void setPresenceStatus(int presenceStatus) {
        this.presenceStatus = presenceStatus;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static class PlatformBean {
        /**
         * createTime :
         * endWordFour :
         * endWordOne :
         * endWordThree :
         * endWordTwo :
         * id : 0
         * isAndroid : 0
         * isQuick : 0
         * name :
         * password :
         * phone :
         * platformType :
         * presenceStatus : 0
         * reservationPath :
         * startWordFour :
         * startWordOne :
         * startWordThree :
         * startWordTwo :
         * status : 0
         * updateTime :
         * username :
         * wechatPath :
         * wechatQc :
         */

        private String createTime;
        private String endWordFour;
        private String endWordOne;
        private String endWordThree;
        private String endWordTwo;
        private int id;
        private int isAndroid;//	平台是否有预约机 0-没有 1-有
        private int isQuick;//	平台是否开启快速通道 0-关闭 1-开启
        private String name;
        private String password;
        private String phone;//子平台总负责人联系人电话
        private String platformType;//平台类型,可用值:OFFICEBUILDING,PLOT,COMMAND
        private int presenceStatus;
        private String reservationPath;
        private String startWordFour;
        private String startWordOne;
        private String startWordThree;
        private String startWordTwo;
        private int status;//平台是启用还是禁用 0-禁用 1-启用
        private String updateTime;
        private String username;
        private String wechatPath;
        private String wechatQc;

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getEndWordFour() {
            return endWordFour;
        }

        public void setEndWordFour(String endWordFour) {
            this.endWordFour = endWordFour;
        }

        public String getEndWordOne() {
            return endWordOne;
        }

        public void setEndWordOne(String endWordOne) {
            this.endWordOne = endWordOne;
        }

        public String getEndWordThree() {
            return endWordThree;
        }

        public void setEndWordThree(String endWordThree) {
            this.endWordThree = endWordThree;
        }

        public String getEndWordTwo() {
            return endWordTwo;
        }

        public void setEndWordTwo(String endWordTwo) {
            this.endWordTwo = endWordTwo;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIsAndroid() {
            return isAndroid;
        }

        public void setIsAndroid(int isAndroid) {
            this.isAndroid = isAndroid;
        }

        public int getIsQuick() {
            return isQuick;
        }

        public void setIsQuick(int isQuick) {
            this.isQuick = isQuick;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPlatformType() {
            return platformType;
        }

        public void setPlatformType(String platformType) {
            this.platformType = platformType;
        }

        public int getPresenceStatus() {
            return presenceStatus;
        }

        public void setPresenceStatus(int presenceStatus) {
            this.presenceStatus = presenceStatus;
        }

        public String getReservationPath() {
            return reservationPath;
        }

        public void setReservationPath(String reservationPath) {
            this.reservationPath = reservationPath;
        }

        public String getStartWordFour() {
            return startWordFour;
        }

        public void setStartWordFour(String startWordFour) {
            this.startWordFour = startWordFour;
        }

        public String getStartWordOne() {
            return startWordOne;
        }

        public void setStartWordOne(String startWordOne) {
            this.startWordOne = startWordOne;
        }

        public String getStartWordThree() {
            return startWordThree;
        }

        public void setStartWordThree(String startWordThree) {
            this.startWordThree = startWordThree;
        }

        public String getStartWordTwo() {
            return startWordTwo;
        }

        public void setStartWordTwo(String startWordTwo) {
            this.startWordTwo = startWordTwo;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getWechatPath() {
            return wechatPath;
        }

        public void setWechatPath(String wechatPath) {
            this.wechatPath = wechatPath;
        }

        public String getWechatQc() {
            return wechatQc;
        }

        public void setWechatQc(String wechatQc) {
            this.wechatQc = wechatQc;
        }
    }
    //http://yinzhen.longpeng.dev.cq1080.com/video/2020/4/8/20200408153606.mp4
}
/**
 * {"code":200,"data":[
 * {"id":26,"createTime":1586339115000,"updateTime":1586339115000,"presenceStatus":1,"url":"images/2020/4/8/20200408174511.jpeg","name":"阿斯蒂芬","note":"阿斯蒂芬","platformId":1,"contentType":"IMAGE","playSpeed":"QUICK","conversionForm":"FlipVerticalTransformer","platform":null},
 * {"id":17,"createTime":1586338923000,"updateTime":1586338923000,"presenceStatus":1,"url":"images/2020/4/8/20200408174201.jpeg","name":"阿斯蒂芬","note":"阿斯蒂芬","platformId":1,"contentType":"IMAGE","playSpeed":"QUICK","conversionForm":"FlipVerticalTransformer","platform":null},
 * {"id":18,"createTime":1586338933000,"updateTime":1586338941000,"presenceStatus":1,"url":"images/2020/4/8/20200408174220.jpeg","name":"阿斯蒂芬","note":"阿斯蒂芬","platformId":1,"contentType":"IMAGE","playSpeed":"QUICK","conversionForm":"FlipVerticalTransformer","platform":null},
 * {"id":22,"createTime":1586339066000,"updateTime":1586339066000,"presenceStatus":1,"url":"images/2020/4/8/20200408174418.jpg","name":"阿斯蒂芬","note":"阿斯蒂芬","platformId":1,"contentType":"IMAGE","playSpeed":"MEDIUM","conversionForm":"FlipHorizontalTransformer","platform":null},
 * {"id":25,"createTime":1586339100000,"updateTime":1586339100000,"presenceStatus":1,"url":"images/2020/4/8/20200408174458.jpeg","name":"阿斯蒂芬","note":"阿斯蒂芬","platformId":1,"contentType":"IMAGE","playSpeed":"QUICK","conversionForm":"FlipVerticalTransformer","platform":null},
 * {"id":3,"createTime":1586330929000,"updateTime":1586331371000,"presenceStatus":1,"url":"video/2020/4/8/20200408153606.mp4","name":"视频222","note":"阿斯蒂芬222","platformId":1,"contentType":"VIDEO","playSpeed":"SLOW","conversionForm":"ZoomInTransformer","platform":null}],"msg":"success"}
 */