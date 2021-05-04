package com;

import com.Controller.ComicController;
import com.config.SensitiveWordInit;

import com.dao.ArticleMappper;

import com.pojo.Article;
import com.pojo.KeyWords;
import com.service.MenuService;

import com.untils.AnsjUntils;
import com.untils.SensitivewordUtils;
import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.recognition.impl.StopRecognition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@SpringBootTest
class SpringbootWebApplicationTests {

    @Autowired
    MenuService menuService;

    @Autowired
    ComicController comicController;

    @Test
    void contextLoads() {
       // comicController.addcomic(new Comic("sad","sad"));
    }

    @Test
    public void test(){
        String w = new String("你好");
        KeyWords ss = new KeyWords(w);
        List list = new ArrayList();
        list.add(ss);

        SensitiveWordInit sensitiveWordInit = new SensitiveWordInit();
        Map sensitiveWordMap   = sensitiveWordInit.initKeyWord(list);
        /*

        for(Map.Entry entry : sensitiveWordMap.entrySet()){
            String mapKey = entry.getKey();
            String mapValue = entry.getValue();
            System.out.println(mapKey+":"+mapValue);
        }
         */



        //传入SensitivewordEngine类中的敏感词库
        SensitivewordUtils.sensitiveWordMap = sensitiveWordMap;

        boolean ans = SensitivewordUtils.isContaintSensitiveWord("不你棒好",2);
        System.out.println(ans);
        /*for(Map.Entry<String, Integer>  entry : ans.entrySet()){
            String mapKey = entry.getKey();
            Integer mapValue = entry.getValue();
            System.out.println(mapKey+":"+mapValue);
        }

         */

    }

    @Autowired
    ArticleMappper articleMappper;
    @Test
    public void test1() throws IOException {
        Set<String> expectedNature = new HashSet<String>() {{
            add("n");add("v");add("vd");add("vn");add("vf");
            add("vx");add("vi");add("vl");add("vg");
            add("nt");add("nz");add("nw");add("nl");
            add("ng");add("userDefine");add("wh");
        }};
        Article article= articleMappper.getArticlebyId(28,1);
        Result result = ToAnalysis.parse(article.getContentMd()); //分词结果的一个封装，主要是一个List<Term>的terms
        //System.out.println(result.getTerms());

        List<Term> terms = result.getTerms(); //拿到terms
        //System.out.println(terms.size());
        String str = "";
        for(int i=0; i<terms.size(); i++) {
            String word = terms.get(i).getName(); //拿到词
            String natureStr = terms.get(i).getNatureStr(); //拿到词性
            if(expectedNature.contains(natureStr)) {
                //System.out.println(word + ":" + natureStr);
                str+=word;
            }
        }


        KeyWordComputer kwc = new KeyWordComputer(10);
        String title = article.getTitle();
        String content = str;
        Collection<Keyword> results = kwc.computeArticleTfidf(title, content);
        Set<String>list = new HashSet<>();
        for(Keyword s : results){
            System.out.println(s.getName()+" "+s.getFreq()+" "+s.getScore());
            list.add(s.getName());
        }
        //System.out.println(results);


        article= articleMappper.getArticlebyId(26,1);
        result = ToAnalysis.parse(article.getContentMd()); //分词结果的一个封装，主要是一个List<Term>的terms
        //System.out.println(result.getTerms());

        terms = result.getTerms(); //拿到terms
        //System.out.println(terms.size());
        str = "";
        for(int i=0; i<terms.size(); i++) {
            String word = terms.get(i).getName(); //拿到词
            String natureStr = terms.get(i).getNatureStr(); //拿到词性
            if(expectedNature.contains(natureStr)) {
                //System.out.println(word + ":" + natureStr);
                str+=word;
            }
        }


        kwc = new KeyWordComputer(10);
        title = article.getTitle();
        content = str;
        Collection<Keyword> results2 = kwc.computeArticleTfidf(title, content);
        for(Keyword s : results2){
            System.out.println(s.getName()+" "+s.getFreq()+" "+s.getScore());
            list.add(s.getName());
        }
        List<Double> arr = new ArrayList<>();
        for(Keyword s : results){
            if(list.contains(s.getName())){
                arr.add(Double.valueOf(s.getFreq()));
            }
            else{
                arr.add(0.0);
            }
        }


        List<Double>brr = new ArrayList<>();
        for(Keyword s : results2){
            if(list.contains(s.getName())){
                brr.add(Double.valueOf(s.getFreq()));
            }
            else{
                brr.add(0.0);
            }
        }

        double sum1 = 0.0;
        for(int i=0; i<10; i++){
            sum1 += arr.get(i) * brr.get(i);
        }

        double sum2 = 0.0;
        for(Double d : arr){
            sum2 += d*d;
        }

        double sum3 = 0.0;
        for(Double d : brr){
            sum3 += d*d;
        }

        System.out.println(sum1/(Math.sqrt(sum2)*Math.sqrt(sum3)));

    }

    @Test
    public void test2() throws IOException {
        Article article1 = articleMappper.getArticlebyId(26,1);
        Article article2 = articleMappper.getArticlebyId(28,1);
        AnsjUntils ansj = new AnsjUntils();
        System.out.println(ansj.isLike(article1,article2));
    }



    @Test
    public void test3() throws IOException {

        Article article = articleMappper.getArticlebyId(28,1);
        Article article1 = articleMappper.getArticlebyId(26,1);
        AnsjUntils ansj = new AnsjUntils();
        System.out.println(ansj.checkSameKeyWord(article,article1));








    }



}
