package com.mp.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mp.common.bean.ware.Purchase;
import com.mp.common.bean.ware.PurchaseDetail;
import com.mp.common.constant.WareConstant;
import com.mp.common.utils.PageUtils;
import com.mp.common.utils.Query;
import com.mp.ware.mapper.PurchaseMapper;
import com.mp.ware.service.PurchaseDetailService;
import com.mp.ware.service.PurchaseService;
import com.mp.ware.service.WareSkuService;
import com.mp.ware.vo.MergeVo;
import com.mp.ware.vo.PurchaseDoneVo;
import com.mp.ware.vo.PurchaseItemDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseMapper, Purchase> implements PurchaseService {

    @Autowired
    private PurchaseDetailService purchaseDetailService;

    @Autowired
    private WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<Purchase> page = this.page(
                new Query<Purchase>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnreceivePurchase(Map<String, Object> params) {
        IPage<Purchase> page = this.page(
                new Query<Purchase>().getPage(params),
                new QueryWrapper<Purchase>().eq("status",0).or().eq("status",1)
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void mergePurchase(MergeVo mergeVo) {
        Long purchaseId = mergeVo.getPurchaseId();
        if(purchaseId == null){
            //1、新建一个
            Purchase purchase = new Purchase();

            purchase.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
            purchase.setCreateTime(new Date());
            purchase.setUpdateTime(new Date());
            this.save(purchase);
            purchaseId = purchase.getId();
        }

        //TODO 确认采购单状态是0,1才可以合并

        List<Long> items = mergeVo.getItems();
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetail> collect = items.stream().map(i -> {
            PurchaseDetail detail = new PurchaseDetail();

            detail.setId(i);
            detail.setPurchaseId(finalPurchaseId);
            detail.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
            return detail;
        }).collect(Collectors.toList());


        purchaseDetailService.updateBatchById(collect);

        Purchase purchase = new Purchase();
        purchase.setId(purchaseId);
        purchase.setUpdateTime(new Date());
        this.updateById(purchase);
    }

    /**
     *
     * @param ids 采购单id
     */
    @Override
    public void received(List<Long> ids) {
        //1、确认当前采购单是新建或者已分配状态
        List<Purchase> collect = ids.stream().map(id -> {
            Purchase byId = this.getById(id);
            return byId;
        }).filter(item -> {
            if (item.getStatus() == WareConstant.PurchaseStatusEnum.CREATED.getCode() ||
                    item.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode()) {
                return true;
            }
            return false;
        }).map(item->{
            item.setStatus(WareConstant.PurchaseStatusEnum.RECEIVE.getCode());
            item.setUpdateTime(new Date());
            return item;
        }).collect(Collectors.toList());

        //2、改变采购单的状态
        this.updateBatchById(collect);



        //3、改变采购项的状态
        collect.forEach((item)->{
            List<PurchaseDetail> entities = purchaseDetailService.listDetailByPurchaseId(item.getId());
            List<PurchaseDetail> detailEntities = entities.stream().map(entity -> {
                PurchaseDetail entity1 = new PurchaseDetail();
                entity1.setId(entity.getId());
                entity1.setStatus(WareConstant.PurchaseDetailStatusEnum.BUYING.getCode());
                return entity1;
            }).collect(Collectors.toList());
            purchaseDetailService.updateBatchById(detailEntities);
        });
    }

    @Transactional
    @Override
    public void done(PurchaseDoneVo doneVo) {

        Long id = doneVo.getId();


        //2、改变采购项的状态
        Boolean flag = true;
        List<PurchaseItemDoneVo> items = doneVo.getItems();

        List<PurchaseDetail> updates = new ArrayList<>();
        for (PurchaseItemDoneVo item : items) {
            PurchaseDetail detail = new PurchaseDetail();
            if(item.getStatus() == WareConstant.PurchaseDetailStatusEnum.HASERROR.getCode()){
                flag = false;
                detail.setStatus(item.getStatus());
            }else{
                detail.setStatus(WareConstant.PurchaseDetailStatusEnum.FINISH.getCode());
                ////3、将成功采购的进行入库
                PurchaseDetail entity = purchaseDetailService.getById(item.getItemId());
                wareSkuService.addStock(entity.getSkuId(),entity.getWareId(),entity.getSkuNum());

            }
            detail.setId(item.getItemId());
            updates.add(detail);
        }

        purchaseDetailService.updateBatchById(updates);

        //1、改变采购单状态
        Purchase purchase = new Purchase();
        purchase.setId(id);
        purchase.setStatus(flag?WareConstant.PurchaseStatusEnum.FINISH.getCode():WareConstant.PurchaseStatusEnum.HASERROR.getCode());
        purchase.setUpdateTime(new Date());
        this.updateById(purchase);




    }

}
