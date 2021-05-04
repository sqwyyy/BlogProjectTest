package com.untils;

import com.pojo.Article;
import io.swagger.models.auth.In;
import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.recognition.impl.StopRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.tire.library.Library;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @date 2021/4/23 - 15:10
 */
@Configuration
public class AnsjUntils {
    public static List<Term> segWord(String text) throws IOException {
        StopRecognition filter = new StopRecognition();
        String stopWordTable = "stop_words.txt";
        File f = new File("D:/workspace/springboot-web/src/main/resources",stopWordTable);
        FileInputStream fileInputStream = new FileInputStream(f);
        //读入停用词文件
        BufferedReader StopWordFileBr = new BufferedReader(new InputStreamReader(fileInputStream));
        String stopWord = null;
        String regEx = "[\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(regEx);
        for(; (stopWord = StopWordFileBr.readLine()) != null;){
            Matcher matcher = pat.matcher(stopWord);
            if (matcher.find()){
                filter.insertStopWords(stopWord);
            }
            else{
                filter.insertStopNatures(stopWord);
            }
            //System.out.println(stopWord);
            //filter.insertStopWords(stopWord);;
        }
        StopWordFileBr.close();

        Forest forest = null;
        try {
            //加载自定义字典文件
            forest = Library.makeForest(AnsjUntils.class.getResourceAsStream("/library/userLibrary.dic"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //使用anjs精准分词模式
        Result result = ToAnalysis.parse(text, forest).recognition(filter);;
        List<Term> termList = result.getTerms();
        return termList;

    }

    public Collection<Keyword> segment(Article article) throws IOException {
        List<Term> terms = segWord(article.getContentMd());
        KeyWordComputer kwc = new KeyWordComputer(10);
        String title = article.getTitle();
        String content = terms.toString();
        Collection<Keyword> results = kwc.computeArticleTfidf(title, content);
        return results;
    }

    public boolean checkSameKeyWord(Article article1, Article article2) throws IOException {
        Collection<Keyword> results = segment(article1);
        Collection<Keyword> results2 = segment(article2);
        Set<String>vis = new HashSet<>();
        for(Keyword s : results){
            vis.add(s.getName());
        }
        boolean flag = false;
        for(Keyword s : results2){
            if(vis.contains(s.getName())){
                flag = true;
            }
        }
        return flag;
    }


    public static Double getCos(Set<String>list,Collection<Keyword> results,Collection<Keyword> results2){
        List<Double> arr = new ArrayList<>();
        List<Double> brr = new ArrayList<>();
        Map<String, Integer> vis = new HashMap<>();
        Integer i = 0;
        for(Keyword s : results){
            if(list.contains(s.getName())){
                arr.add(Double.valueOf(s.getFreq()));
                vis.put(s.getName(),i);
            }
            else{
                arr.add(0.0);
            }
            i++;
        }
        i=0;
        for(int j=0;j<10;j++){
            brr.add(0.0);
        }
        for(Keyword ss : results2){
            if(list.contains(ss.getName())){
                brr.set(vis.get(ss.getName()),Double.valueOf(ss.getFreq()));
            }
            i++;
        }
        double sum1 = 0.0;
        for(int j=0; j<10; j++){
            sum1 += arr.get(j) * brr.get(j);
        }

        double sum2 = 0.0;
        for(Double d : arr){
            sum2 += d*d;
        }

        double sum3 = 0.0;
        for(Double d : brr){
            sum3 += d*d;
        }
        return sum1/(Math.sqrt(sum2)*Math.sqrt(sum3));
    }


    public double isLike(Article article1, Article article2) throws IOException {
        Collection<Keyword> results = segment(article1);
        Collection<Keyword> results2 = segment(article2);
        Set<String>list = new HashSet<>();
        Set<String>ans = new HashSet<>();
        for(Keyword s : results){
            list.add(s.getName());
        }
        for(Keyword s : results2){
            if(list.contains(s.getName())){
                ans.add(s.getName());
            }
        }
        return getCos(ans,results,results2);

    }
}
