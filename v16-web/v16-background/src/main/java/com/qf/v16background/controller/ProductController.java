package com.qf.v16background.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.qf.api.ISearchService;
import com.qf.v16.api.IProductService;
import com.qf.v16.api.vo.ProductVO;
import com.qf.v16.common.pojo.ResultBean;
import com.qf.v16.entity.TProduct;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jws.WebParam;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author huangguizhao
 */
@Controller
@RequestMapping("product")
public class ProductController {

    @Reference
    private IProductService productService;

    @Reference
    private ISearchService searchService;

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

    @RequestMapping("add")
    public String add(ProductVO vo){//vo view Object
        Long newId = productService.add(vo);
        //TODO 后续作为通知其他系统做相关操作的标志

        //调用搜索服务的更新接口
        searchService.updateById(newId);

        //调用http接口
        //HttpClient Apache

        return "redirect:/product/page/1/1";
    }

    //返回json，适合异步交互的情况
    @RequestMapping("delById/{id}")
    @ResponseBody
    public ResultBean delById(@PathVariable("id") Long id){
        int count = productService.deleteByPrimaryKey(id);
        if(count > 0){
            return new ResultBean("200","删除成功！");
        }
        return new ResultBean("404","删除失败！您的操作有误！");
    }

}
