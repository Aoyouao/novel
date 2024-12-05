package com.aoyouao.novel.core.common.resp;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * 分页响应数据格式封装
 */

@Getter
@AllArgsConstructor
public class PageRestDto<T> {

    private final long pageNum;

    private final long pageSize;

    //总记录数
    private final long total;

    //分页数据集
    private final List<? extends T> list;


    //泛型方法 用于创建和返回一个分页响应dto对象
    public static <T> PageRestDto<T> of(long pageNum,long pageSize,long total,List<T> list){
        return new PageRestDto<>(pageNum,pageSize,total,list);
    }

    //获取分页数
    public long getPages(){
        if(this.pageSize == 0){
            return 0;
        }
        long pages = this.total / this.pageSize;
        if(this.total % this.pageSize != 0){
            ++pages;
        }
        return pages;
    }
}
