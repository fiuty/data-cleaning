package com.chebianjie.datacleaning.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 场地推荐\n@author Lee\n@Date 2020/02/15
 */
@Entity
@Table(name = "ut_site_recommend")
@Data
public class UtSiteRecommend implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    @Column(name = "consumer_id")
    private Long consumerId;

    @Column(name = "agent_id")
    @ApiModelProperty("商家id")
    private Long agentId;


    /**
     * 用户手机号
     */
    @NotNull
    @ApiModelProperty(value = "用户手机号")
    @Column(name = "tel")
    private String tel;

    /**
     * 推荐人姓名
     */
    @ApiModelProperty(value = "推荐人姓名")
    @Column(name = "recommend_name")
    private String recommendName;

    /**
     * 推荐人手机号
     */
    @ApiModelProperty(value = "推荐人手机号")
    @Column(name = "recommend_tel")
    private String recommendTel;

    /**
     * 省
     */
    @ApiModelProperty(value = "省")
    @Column(name = "province")
    private String province;

    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    @Column(name = "city")
    private String city;

    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址")
    @Column(name = "address")
    private String address;

    /**
     * 经度
     */
    @ApiModelProperty(value = "经度")
    @Column(name = "lng")
    private String lng;

    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度")
    @Column(name = "lat")
    private String lat;

    /**
     * 类型,1加油站,2社区楼盘,3社会停车场
     */
    @ApiModelProperty(value = "类型,1加油站,2社区楼盘,3社会停车场")
    @Column(name = "recommend_type")
    private Integer recommendType;

    /**
     * 位置,1城市核心区20分,2城市外围18分,3县城中心16分,4县城外围15分,5国省道旁10分,6乡镇5分
     */
    @ApiModelProperty(value = "位置,1城市核心区20分,2城市外围18分,3县城中心16分,4县城外围15分,5国省道旁10分,6乡镇5分")
    @Column(name = "position")
    private Integer position;

    /**
     * 日汽油销量情况,1,25吨以上30分,2,20-25吨25分,3,15-25吨20分,4,10-15吨15分,5,5-10吨10分,6,5吨以下5分; 停车场评估,7,5000户以上30分,8,3000户以上25分,9,2000户以上20分,10,1000户以上15分; 社会停车场,11,200车位以上30分,12,100车位以上25分,13,50车位以上20分,14,50车位以下15分 ;
     */
    @ApiModelProperty(value = "日汽油销量情况,1,25吨以上20分,2,20-25吨25分,3,15-25吨20分,4,10-15吨15分,5,5-10吨10分,6,5吨以下5分; 停车场评估,7,5000户以上30分,8,3000户以上25分,9,2000户以上20分,10,1000户以上15分; 社会停车场,11,200车位以上30分,12,100车位以上25分,13,50车位以上20分,14,50车位以下15分 ;")
    @Column(name = "gasoline_sales")
    private Integer gasolineSales;

    /**
     * 周边商圈,1高档小区密度高20分,2中低档小区集中15分,3居住人口分散10分,4远离人口集中区5分
     */
    @ApiModelProperty(value = "周边商圈,1高档小区密度高20分,2中低档小区集中15分,3居住人口分散10分,4远离人口集中区5分")
    @Column(name = "business_district")
    private Integer businessDistrict;

    /**
     * 建设成本,1水电距离30-50米20分,2水电一项超过50米15分,3水电一项超过100米10分
     */
    @ApiModelProperty(value = "建设成本,1水电距离30-50米20分,2水电一项超过50米15分,3水电一项超过100米10分")
    @Column(name = "cost")
    private Integer cost;

    /**
     * 建设位置,1油站出口10分,2油站入口5分,3小区外围10分,4小区内5分,5靠近出口10分,6靠近入口5分
     */
    @ApiModelProperty(value = "建设位置,1油站出口10分,2油站入口5分,3小区外围10分,4小区内5分,5靠近出口10分,6靠近入口5分")
    @Column(name = "construction_location")
    private Integer constructionLocation;

    /**
     * 供水方式,1自来水,2井水
     */
    @ApiModelProperty(value = "供水方式,1自来水,2井水")
    @Column(name = "water_type")
    private Integer waterType;

    /**
     * 市政排污,1有,2没有
     */
    @ApiModelProperty(value = "市政排污,1有,2没有")
    @Column(name = "blowdown")
    private Integer blowdown;

    /**
     * 供电电压
     */
    @ApiModelProperty(value = "市供电电压,1:220V")
    @Column(name = "voltage")
    private Integer voltage;

    /**
     * 场地照片 / 视频
     */
    @ApiModelProperty(value = "场地照片 / 视频")
    @Column(name = "photo")
    private String photo;

    /**
     * 特殊情况描述
     */
    @ApiModelProperty(value = "特殊情况描述")
    @Column(name = "description")
    private String description;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间/等待审核时间")
    @Column(name = "create_time")
    private Long createTime;

    /**
     * 审核状态,1等待审核,2已考察,3正在施工,4已建站
     */
    @ApiModelProperty(value = "审核状态,1等待审核,2已考察,3正在施工,4已建站")
    @Column(name = "status")
    private Integer status;

    /**
     * 考察时间
     */
    @ApiModelProperty(value = "考察时间")
    @Column(name = "inspection_time")
    private Long inspectionTime;

    /**
     * 施工时间
     */
    @ApiModelProperty(value = "施工时间")
    @Column(name = "construction_time")
    private Long constructionTime;

    /**
     * 建站时间
     */
    @ApiModelProperty(value = "建站时间")
    @Column(name = "setup_time")
    private Long setupTime;

    /**
     * 得分
     */
    @ApiModelProperty(value = "得分")
    @Column(name = "score")
    private Integer score;


    @ApiModelProperty(value = "建设标准 3: 3机位  2: 2机位")
    @Column(name = "construction_standard")
    private Integer constructionStandard;

    @ApiModelProperty(value = "用户唯一标识")
    @Column(name = "consumer_union_account")
    private String consumerUnionAccount;


}
