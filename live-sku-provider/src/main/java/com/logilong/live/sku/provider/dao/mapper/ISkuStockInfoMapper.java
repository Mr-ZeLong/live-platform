package com.logilong.live.sku.provider.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.logilong.live.sku.provider.dao.po.SkuStockInfoPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ISkuStockInfoMapper extends BaseMapper<SkuStockInfoPO> {

    @Update("update t_sku_stock_info set stock_num =  #{stock_num} where skuId = #{skuId} and version = #{version}")
    int updateStockNumBySkuId(@Param("skuId") Long skuId, @Param("num") Integer stock_num, @Param("version") Integer version);

    @Update("update t_sku_stock_info set stock_num = stock_num - #{num} where skuId = #{skuId} and stock_num - #{num} >= 0 and version = #{version}")
    int decrStockNumBySkuId(@Param("skuId") Long skuId, @Param("num") Integer num, @Param("version") Integer version);
}
