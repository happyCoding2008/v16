package com.qf.v16background.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.qf.v16.api.IProductService;
import com.qf.v16.entity.TProduct;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jws.WebParam;
import java.util.List;

/**
 * @author huangguizhao
 */
@Controller
@RequestMapping("product")
public class ProductController {

    @Reference
    private IProductService productService;

    @RequestMapping("getById/{id}")
    @ResponseBody
    public TProduct getById(@PathVariable("id") Long id){
        return productService.selectByPrimaryKey(id);
    }

    @RequestMapping("list")
    public String list(Model model){
        //1.獲取商品的信息
        List<TProduct> list = productService.list();
        //2.跳轉到頁面進行展示
        model.addAttribute("list",list);
        return "product/list";
    }

    @RequestMapping("page/{pageIndex}/{pageSize}")
    public String page(Model model,
                       @PathVariable("pageIndex") Integer pageIndex,
                       @PathVariable("pageSize") Integer pageSize){
        //1.獲取到分頁的信息
        PageInfo<TProduct> pageInfo = productService.page(pageIndex, pageSize);
        //2.跳轉到頁面進行展示
        model.addAttribute("pageInfo",pageInfo);
        return "product/list";
    }
}
