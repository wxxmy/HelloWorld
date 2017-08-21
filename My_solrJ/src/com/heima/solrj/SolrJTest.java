package com.heima.solrj;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class SolrJTest {
    @Test
    public void saveOrUpdate() throws SolrServerException, IOException{
        //指定远程solr服务器地址
        //1.当索引库是默认名称collection1的时候可以不写索引库名称
        //2.当索引库名称不是默认名称,必须写上索引库名称
        String baseURL = "http://localhost:8080/solr/article";
        //创建solr核心对象连接远程solr服务
        SolrServer solrServer = new HttpSolrServer(baseURL);
        
        //创建一个文档对象
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id", "1010100");
        document.addField("title", "微信个人号python1");
        
        //添加索引库
        solrServer.add(document);
        //提交
        solrServer.commit();
    }
    @Test
    public void deleteById() throws SolrServerException, IOException{
        //指定远程solr服务器地址
        //1.当索引库是默认名称collection1的时候可以不写索引库名称
        //2.当索引库名称不是默认名称,必须写上索引库名称
        String baseURL = "http://localhost:8080/solr/article";
        //创建solr核心对象连接远程solr服务
        SolrServer solrServer = new HttpSolrServer(baseURL);
        
        //创建一个文档对象
        solrServer.deleteById("1010100");
        //提交
        solrServer.commit();
    }
    @Test
    public void deleteByQuery() throws SolrServerException, IOException{
        //指定远程solr服务器地址
        //1.当索引库是默认名称collection1的时候可以不写索引库名称
        //2.当索引库名称不是默认名称,必须写上索引库名称
        String baseURL = "http://localhost:8080/solr/article";
        //创建solr核心对象连接远程solr服务
        SolrServer solrServer = new HttpSolrServer(baseURL);
        
        //创建一个文档对象
        solrServer.deleteByQuery("*.*");
        //提交
        solrServer.commit();
    }
}
