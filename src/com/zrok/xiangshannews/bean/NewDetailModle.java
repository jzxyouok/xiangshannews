
package com.zrok.xiangshannews.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class NewDetailModle implements Serializable {
	
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * docid
     */
    private String docid;
    /**
     * title
     */
    private String title;
    /**
     * source
     */
    private String source;
    /**
     * body
     */
    private String body;
    /**
     * ptime
     */
    private String ptime;
    /**
     * cover
     */
    private String cover;
    /**
     * url_mp4
     */
    private String url_mp4;
    /**
     * 图片列表
     */
    private List<String> imgList;

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getUrl_mp4() {
        return url_mp4;
    }

    public void setUrl_mp4(String url_mp4) {
        this.url_mp4 = url_mp4;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public List<String> getImgList() {
        return imgList;
    }

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
    }
    
    public static NewDetailModle readJsonNewDetailModles(String res, String newId) {
    	NewDetailModle newDetailModle=null;
        try {
            if (res == null || res.equals("")) {
                return null;
            }
            JSONObject jsonObject = new JSONObject(res).getJSONObject(newId);
            newDetailModle = readNewModle(jsonObject);
        } catch (Exception e) {

        } finally {
            System.gc();
        }
        return newDetailModle;
    }

    /**
     * 解析图片集
     * 
     * @param jsonArray
     * @return
     * @throws Exception
     */
    private static List<String> readImgList(JSONArray jsonArray) throws Exception {
        List<String> imgList = new ArrayList<String>();

        for (int i = 0; i < jsonArray.length(); i++) {
            imgList.add(jsonArray.getJSONObject(i).optString("src"));
        }

        return imgList;
    }

    /**
     * 获取图文列表
     * 
     * @param jsonObject
     * @return
     * @throws Exception
     */
    private static NewDetailModle readNewModle(JSONObject jsonObject) throws Exception {
        NewDetailModle newDetailModle = null;

        String docid = "";
        String title = "";
        String source = "";
        String ptime = "";
        String body = "";
        String url_mp4 = "";
        String cover = "";

        docid = jsonObject.optString("docid");
        title = jsonObject.optString("title");
        source = jsonObject.optString("source");
        ptime = jsonObject.optString("ptime");
        body = jsonObject.optString("body");

        if (jsonObject.has("video")) {
            JSONObject jsonObje = jsonObject.getJSONArray("video").getJSONObject(0);
            url_mp4 = jsonObje.optString("url_mp4");
            cover = jsonObje.optString("cover");
        }

        JSONArray jsonArray = jsonObject.getJSONArray("img");

        List<String> imgList = readImgList(jsonArray);

        newDetailModle = new NewDetailModle();

        newDetailModle.setDocid(docid);
        newDetailModle.setImgList(imgList);
        newDetailModle.setPtime(ptime);
        newDetailModle.setSource(source);
        newDetailModle.setTitle(title);
        newDetailModle.setBody(body);
        newDetailModle.setUrl_mp4(url_mp4);
        newDetailModle.setCover(cover);

        return newDetailModle;
    }

    
    
}
