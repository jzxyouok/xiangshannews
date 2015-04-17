
package com.zrok.xiangshannews.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class NewModle implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * docid
     */
    private String docid;
    /**
     * 标题
     */
    private String title;
    /**
     * 小内容
     */
    private String digest;
    /**
     * 图片地址
     */
    private String imgsrc;
    /**
     * 来源
     */
    private String source;
    /**
     * 时间
     */
    private String ptime;
    /**
     * TAG
     */
    private String tag;
    /**
     * 列表
     */
    private ImagesModle imagesModle;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public ImagesModle getImagesModle() {
        return imagesModle;
    }

    public void setImagesModle(ImagesModle imagesModle) {
        this.imagesModle = imagesModle;
    }

    /**
     * 头部列表
     */
    private List<ImagesModle> imgHeadLists;

    public List<ImagesModle> getImgHeadLists() {
        return imgHeadLists;
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

    public void setImgHeadLists(List<ImagesModle> imgHeadLists) {
        this.imgHeadLists = imgHeadLists;
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

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }
    
    
    public static List<NewModle> readJsonNewModles(String res, String value){
    	List<NewModle>  newModles = new ArrayList<NewModle>();
        try {
            if (res == null || res.equals("")) {
                return null;
            }
            NewModle newModle = null;
            JSONObject jsonObject = new JSONObject(res);
            JSONArray jsonArray = jsonObject.getJSONArray(value);
            // if (isFirst) {
            // for (int i = 0; i < 4; i++) {
            // JSONObject js = jsonArray.getJSONObject(i);
            // newModle = readNewModle(js);
            // newModles.add(newModle);
            // }
            // }

            for (int i = 1; i < jsonArray.length(); i++) {
                newModle = new NewModle();
                JSONObject js = jsonArray.getJSONObject(i);
                if (js.has("skipType") && js.getString("skipType").equals("special")) {
                    continue;
                }
                if (js.has("TAGS") && !js.has("TAG")) {
                    continue;
                }
                if (js.has("imgextra")) {
                    newModle.setTitle(js.optString("title"));
                    newModle.setDocid(js.optString("docid"));
                    ImagesModle imagesModle = new ImagesModle();
                    List<String> list;
                    list = readImgList(js.getJSONArray("imgextra"));
                    list.add(js.optString("imgsrc"));
                    imagesModle.setImgList(list);
                    newModle.setImagesModle(imagesModle);
                } else {
                    newModle = readNewModle(js);
                }
                newModles.add(newModle);
            }
        } catch (Exception e) {

        } finally {
            System.gc();
        }
        return newModles;
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
            imgList.add(jsonArray.getJSONObject(i).optString("imgsrc"));
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
    private static NewModle readNewModle(JSONObject jsonObject) throws Exception {
        NewModle newModle = null;

        String docid = "";
        String title = "";
        String digest = "";
        String imgsrc = "";
        String source = "";
        String ptime = "";
        String tag = "";

        docid = jsonObject.optString("docid");
        title = jsonObject.optString("title");
        digest = jsonObject.optString("digest");
        imgsrc = jsonObject.optString("imgsrc");
        source = jsonObject.optString("source");
        ptime = jsonObject.optString("ptime");
        tag = jsonObject.optString("TAG");

        newModle = new NewModle();

        newModle.setDigest(digest);
        newModle.setDocid(docid);
        newModle.setImgsrc(imgsrc);
        newModle.setTitle(title);
        newModle.setPtime(ptime);
        newModle.setSource(source);
        newModle.setTag(tag);

        return newModle;
    }
}
