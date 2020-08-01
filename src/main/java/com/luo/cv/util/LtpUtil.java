package com.luo.cv.util;

import edu.hit.ir.ltp4j.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shkstart
 * @create 2020-07-26 13:45
 */
public class LtpUtil {
    private static final Logger logger = LoggerFactory.getLogger(LtpUtil.class);

    private static String MODEL_PATH = "D:/LTP/ltp_data/";

    private static Segmentor segmentor;

    private static Postagger postagger;

    private static NER ner;

    private static Parser parser;

    private static SRL srl;

    /**
     * 分词
     * @param sentence
     * @return
     */
    public static List<String> executeSegmentors(String sentence){
        if (segmentor == null)
        {
            segmentor = new Segmentor();
        }

        // 加载模型
        if (segmentor.create(MODEL_PATH + "cws.model") < 0){
            logger.error("segmentor load failed !");
            return null;
        }

        List<String> words = new ArrayList<>();
        // 执行分词
        segmentor.segment(sentence, words);

        return words;
    }

    /**
     * 词性标注
     * @param words
     * @return
     */
    public static List<String> executePostags(List<String> words){
        if (postagger == null)
        {
            postagger = new Postagger();
        }

        // 加载词性标注模型
        if(postagger.create(MODEL_PATH + "pos.model") < 0) {
            logger.error("postagger load failed !");
            return null;
        }

        List<String> postags = new ArrayList<>();
        // 执行词性标注
        postagger.postag(words, postags);

        return postags;
    }

    /**
     * 命名实体识别
     * @param words
     * @param tags
     * @return
     */
    public static List<String> executeNer(List<String> words, List<String> tags){
        if (ner == null)
        {
            ner = new NER();
        }

        // 加载命名实体识别模型
        if(ner.create(MODEL_PATH + "ner.model") < 0) {
            logger.error("ner load failed !");
            return null;
        }

        List<String> ners = new ArrayList<>();
        ner.recognize(words, tags, ners);

        return ners;
    }

    /**
     * 依存句法分析
     * @param words
     * @param tags
     * @return
     */
    public static Map<String, List> executeParser(List<String> words, List<String> tags){
        if (parser == null)
        {
            parser = new Parser();
        }

        // 加载命名实体识别模型
        if(parser.create(MODEL_PATH + "parser.model") < 0) {
            logger.error("parser load failed !");
            return null;
        }

        List<Integer> heads = new ArrayList<Integer>();
        List<String> deprels = new ArrayList<String>();

        parser.parse(words,tags,heads,deprels);

        Map<String, List> map = new HashMap(2);
        map.put("heads", heads);
        map.put("deprels", deprels);

        return map;
    }

    /**
     * 语义角色标注
     * @param words
     * @param tags
     * @param ners
     * @param heads
     * @param deprels
     * @return
     */
    public List<Pair<Integer, List<Pair<String, Pair<Integer, Integer>>>>> executeSrl
            (List<String> words, List<String> tags, List<String> ners, List<Integer> heads, List<String> deprels )
    {
        if (srl == null)
        {
            srl = new SRL();
        }

        // 加载命名实体识别模型
        if(srl.create(MODEL_PATH + "srl.model") < 0) {
            logger.error("srl load failed !");
            return null;
        }

        List<Pair<Integer, List<Pair<String, Pair<Integer, Integer>>>>> srls =
                new ArrayList<Pair<Integer, List<Pair<String, Pair<Integer, Integer>>>>>();

        srl.srl(words, tags, ners, heads, deprels, srls);

        return srls;
    }


    public static void releaseSegmentor(){
        segmentor.release();
    }

    public static void releasePostags(){
        postagger.release();
    }

    public static void releaseNer(){
        ner.release();
    }

    public static void releaseParser(){
        parser.release();
    }

    public static void releaseSrl(){
        srl.release();
    }

}