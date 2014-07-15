package org.webswing.model.s2c;

import java.io.Serializable;
import java.util.Arrays;

public class JsonWindow implements Serializable {
    private static final long serialVersionUID = 5192907046398894082L;
    
    String id;
    String parentId;
    JsonWindowPartialContent[] content;
    String title;
    Integer posX;
    Integer posY;
    Integer width;
    Integer height;
    Boolean hasFocus;
    Boolean modal;
    Boolean resizable;
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getParentId() {
        return parentId;
    }
    
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    
    public JsonWindowPartialContent[] getContent() {
        return content;
    }
    
    public void setContent(JsonWindowPartialContent[] content) {
        this.content = content;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    
    public Integer getPosX() {
        return posX;
    }

    
    public void setPosX(Integer posX) {
        this.posX = posX;
    }

    
    public Integer getPosY() {
        return posY;
    }

    
    public void setPosY(Integer posY) {
        this.posY = posY;
    }

    public Integer getWidth() {
        return width;
    }
    
    public void setWidth(Integer width) {
        this.width = width;
    }
    
    public Integer getHeight() {
        return height;
    }
    
    public void setHeight(Integer height) {
        this.height = height;
    }
    
    public Boolean getHasFocus() {
        return hasFocus;
    }
    
    public void setHasFocus(Boolean hasFocus) {
        this.hasFocus = hasFocus;
    }
    
    public Boolean getModal() {
        return modal;
    }
    
    public void setModal(Boolean modal) {
        this.modal = modal;
    }
    
    public Boolean getResizable() {
        return resizable;
    }
    
    public void setResizable(Boolean resizable) {
        this.resizable = resizable;
    }

    @Override
    public String toString() {
        return "JsonWindow [id=" + id + ", content=" + Arrays.toString(content) + ", title=" + title + ", posX=" + posX + ", posY=" + posY + ", width=" + width + ", height=" + height + "]";
    }
    
}