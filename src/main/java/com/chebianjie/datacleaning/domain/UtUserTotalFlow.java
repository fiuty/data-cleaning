package com.chebianjie.datacleaning.domain;

import com.chebianjie.datacleaning.domain.enums.Platform;
import com.chebianjie.datacleaning.domain.enums.WashMachineClassModel;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 用户总流水
 * @author 许泽坤
 */
@Entity
@Data
@Table(name = "ut_user_total_flow")
@DynamicInsert
@DynamicUpdate
public class UtUserTotalFlow implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户ID
     */
    @Column(name = "jhi_uid")
    private Long uid;

    /**
     * 变动金额（余额以分为单位）, 变动积分, 变动优惠券
     */
    @Column(name = "amount")
    private  Integer amount;

    /**
     * 原账户余额, 积分余额, 优惠券
     */
    @Column(name = "old_balance")
    private Integer oldBalance;

    /**
     * 新账户余额, 积分余额, 优惠券
     */
    @Column(name = "new_balance")
    private Integer newBalance;

    /**
     * 系统类型(1.洗车系统 2.商城系统  3.门店系统)
     */
    @Column(name = "client_type")
    private Integer clientType;

    /**
     * 资金变动方向(1.充值 ,加积分 ,加代金券 2消费 ,减积分 ,减代金券
     */
    @Column(name = "fund_direction")
    private Integer fundDirection;

    /**
     * 业务类型(1.充值  2洗车  3商城消费  4.退款  5门店消费  6余额清零 8撤销退款 9购买年卡 10增值服务)
     */
    @Column(name = "trx_type")
    private Integer trxType;

    /**
     * 业务拓展类型(11.支付宝支付  12.微信支付 13管理员充值 21 洗车消费  22 车主服务消费  23未启动机器 31 车品商城消费 51门店消费  61余额清零 71余额转移 14评论奖励)
     */
    @Column(name = "trx_expand_type")
    private Integer trxExpandType;

    /**
     * 业务拓展状态,61余额清零申请,62余额清零同意,63余额已转移,64余额已转入;101增值服务-退款中,102增值服务-已退款
     */
    private Integer trxExpandStatus;

    /**
     * 流水类型(1.账户金额 2积分 3优惠券 )
     */
    @Column(name = "flow_type")
    private Integer flowType;

    /**
     * 流水产生时间
     */
    @Column(name = "flow_time")
    private Long flowTime;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Long createTime;

    /**
     * 订单编号（根据业务类型和流水类型不同，查找的编号也不同）
     */
    @Column(name = "order_id")
    private Long orderId;

    /**
     * 预留字符串格式订单号，兼容商城订单号
     */
    @Column(name = "order_num")
    private String orderNum;

    /**
     * 备注,trxExpandStatus为64余额已转入时填写记录转移方的手机号
     */
    @Column(name = "remark")
    private String remark;

    /**
     * 代金券金额（分为单位）
     */
    @Column(name = "coupon_price")
    private String couponPrice;
    /**
     * 版本号
     */
    @Column(name = "version")
    private String version;

    /**
     * 旧的赠送余额
     */
    @Column(name = "old_give_balance")
    private Integer oldGiveBalance;

    /**
     * 新的赠送余额
     */
    @Column(name = "new_give_balance")
    private Integer newGiveBalance;

    /**
     * 变动的赠送余额
     */
    @Column(name = "give_balance")
    private Integer giveBalance;


    /**
     * 用户余额比例，默认100
     */
    @Column(name = "consumer_balance_ratio", nullable = false)
    private Integer consumerBalanceRatio;

    /**
     * 赠送余额比例，默认0
     */
    @Column(name = "consumer_gift_balance_ratio", nullable = false)
    private Integer consumerGiftBalanceRatio;

    /**
     * 流水标题
     */
    @Column(name = "title")
    private Long title;

    /**
     * 真实扣钱用户余额
     */
    @Column(name = "real_balance")
    private Integer realBalance;

    /**
     * 真实扣钱赠送余额
     */
    @Column(name = "real_give_balance")
    private Integer realGiveBalance;

    /**
     * 洗车订单是否评价
     */
    @Column(name = "is_wash_comment")
    private Boolean washComment;

    /**
     * 洗车订单金额
     */
    private Integer washRealPrice;

    /**
     * 洗车订单类型：1-洗车,2-商城,3-大客户消费类型,4-单次消费,5-中石化优惠券,6-年卡消费,7-增值服务代洗
     */
    private Integer washConsumeType;

    /**
     * 洗车订单机器类型：0-自助洗车机 1-吸尘器 2-自动洗车机
     */
    private Integer washMachineType;

    /**
     * 大客户运营商id
     */
    private Long agentId;

    /**
     * 大客户分配的消费金额
     */
    private Integer distributionBalance;

    /**
     * 大客户分配的赠送余额
     */
    private Integer distributionGiveBalance;

    /**
     * 大客户该用户当前累加后的消费余额
     */
    private Integer currentBalance;

    /**
     * 大客户当前用户累加后的赠送余额
     */
    private Integer currentGiveBalance;

    /**
     * 用户已购年卡
     */
    private Long userConsumeCardId;

    /**
     * 消费卡购买批次号
     */
    private String wechatnum;

    /**
     * 优惠券类型
     */
    private Integer couponType;

    /**
     * 充值订单状态（1支付成功，2待支付，3支付失败，4用户取消支付，5已完成）
     */
    private Integer chargeStatus;

    /**
     * 优惠券id
     */
    private Long couponId;

    /**
     * 站点id
     */
    private Long siteId;

    /**
     * 站点名称
     */
    private String siteName;

    /**
     * 流水订单类型
     */
    @Enumerated(EnumType.STRING)
    private WashMachineClassModel washMachineClassModel;

    /**
     * 车服订单进行状态，0待完成-待验证、1待完成-已验证、2超时未验证、3已完成 、4已取消
     */
    private Integer serverOrderStatus;


    /**
     * 用户余额变动操作人
     */
    private String operationName;

    /**
     * 平台,清洗数据用
     */
    @Transient
    private Platform platform;

}
