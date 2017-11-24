package com.eservice.api.model.abnormal_image;

import java.util.Date;
import javax.persistence.*;

@Table(name = "abnormal_image")
public class AbnormalImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "abnormal_record_id")
    private Integer abnormalRecordId;

    /**
     * 异常图片名称（包含路径）,以后这部分数据是最大的，首先pad上传时候时候需要压缩，以后硬盘扩展的话，可以把几几年的图片放置到另外一个硬盘，然后pad端响应升级（根据时间加上图片的路径）
     */
    private String image;

    /**
     * 上传异常图片的时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return abnormal_record_id
     */
    public Integer getAbnormalRecordId() {
        return abnormalRecordId;
    }

    /**
     * @param abnormalRecordId
     */
    public void setAbnormalRecordId(Integer abnormalRecordId) {
        this.abnormalRecordId = abnormalRecordId;
    }

    /**
     * 获取异常图片名称（包含路径）,以后这部分数据是最大的，首先pad上传时候时候需要压缩，以后硬盘扩展的话，可以把几几年的图片放置到另外一个硬盘，然后pad端响应升级（根据时间加上图片的路径）
     *
     * @return image - 异常图片名称（包含路径）,以后这部分数据是最大的，首先pad上传时候时候需要压缩，以后硬盘扩展的话，可以把几几年的图片放置到另外一个硬盘，然后pad端响应升级（根据时间加上图片的路径）
     */
    public String getImage() {
        return image;
    }

    /**
     * 设置异常图片名称（包含路径）,以后这部分数据是最大的，首先pad上传时候时候需要压缩，以后硬盘扩展的话，可以把几几年的图片放置到另外一个硬盘，然后pad端响应升级（根据时间加上图片的路径）
     *
     * @param image 异常图片名称（包含路径）,以后这部分数据是最大的，首先pad上传时候时候需要压缩，以后硬盘扩展的话，可以把几几年的图片放置到另外一个硬盘，然后pad端响应升级（根据时间加上图片的路径）
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * 获取上传异常图片的时间
     *
     * @return create_time - 上传异常图片的时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置上传异常图片的时间
     *
     * @param createTime 上传异常图片的时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}