package com.heima.solrj;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Test;

public class SolrJTest2 {
    @Test
    public void saveOrUpdate() throws SolrServerException, IOException{
        //指定远程solr服务器地址
        //1.当索引库是默认名称collection1的时候可以不写索引库名称
        //2.当索引库名称不是默认名称,必须写上索引库名称
        String baseURL = "http://localhost:8080/solr/products";
        //创建solr核心对象连接远程solr服务
        SolrServer solrServer = new HttpSolrServer(baseURL);
        
        //3.创建查询对象
        SolrQuery solrQuery = new SolrQuery();
        
        //4.1设置主条件查询,查询所有
        //solrQuery.set("q","*:*");
        
        //4.1按细分条件查询,!!!不能单独设置这一个,必须要配合默认查询字段使用,不然查询不到结果
        solrQuery.setQuery("浴室");
        
        //4.2设置过滤条件查询FQ,filter query
        solrQuery.addFilterQuery("product_catalog_name:时尚卫浴");
        
        //4.2添加AND条件的价格在20以下的商品
        solrQuery.addFilterQuery("product_price:[* TO 20]");
        
        //4.3排序:sort,安装价格从高到低排序
        solrQuery.addSort("product_price", ORDER.desc);
        
        //4.4设置起始下标已经每页显示条数
        solrQuery.setStart(0);
        solrQuery.setRows(20);
        
        //4.5设置过滤字段,即写的字段显示,没写的不显示,中间可以用逗号也可以用空格断开
        //solrQuery.setFields("product_name,product_price");//此时商品id,图片,所属种类都是null
        //solrQuery.setFields("product_name product_price");//此时商品id,图片,所属种类都是null
        
        //4.6,df  设置默认查询字段
        solrQuery.set("df", "product_keywords");
        
        //4.7设置高亮显示
        //开启高亮
        solrQuery.setHighlight(true);
        //指定高亮字段
        solrQuery.addHighlightField("product_name");
        //指定高亮前缀
        solrQuery.setHighlightSimplePre("<font color:'red'>");
        //指定高亮后缀
        solrQuery.setHighlightSimplePost("</font>");
        
        //输出查询结果
        QueryResponse queryResponse = solrServer.query(solrQuery);
        
        //获得查询结果,文档集合
        SolrDocumentList results = queryResponse.getResults();
        
        //获得文档总命中数
        long numFound = results.getNumFound();
        System.out.println("文档总查到的数量:"+numFound);
        
        int i = 1;
        for (SolrDocument sDoc : results) {
            System.out.println("------------这是第"+i+"个商品信息---------");
            //获得商品id
            String id = (String) sDoc.get("id");
            System.out.println("商品id是:"+id);
            
            //获得商品名称
            String product_name = (String) sDoc.get("product_name");
            Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
            Map<String, List<String>> map = highlighting.get(id);
            List<String> list = map.get("product_name");
            //判断是否高亮
            if (list !=null &&list.size()>0) {
                product_name = list.get(0);
            }
            System.out.println("商品名称是:"+product_name);
            
            //获得商品图片
            String product_picture = (String) sDoc.get("product_picture");
            System.out.println("商品图片是:"+product_picture);
            
            //获得商品所属种类
            String product_catalog_name = (String) sDoc.get("product_catalog_name");
            System.out.println("商品所属种类是:"+product_catalog_name);
            
            //获得商品价格
            Float product_price = (Float) sDoc.get("product_price");
            System.out.println("商品价格是:"+product_price);
            i++;
        }
    }
}
