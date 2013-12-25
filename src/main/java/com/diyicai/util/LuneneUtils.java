package com.diyicai.util;

import com.diyicai.bean.Searchable;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 构建索引相关的类
 * User: zhangyong
 * Date: 13-12-20
 * Time: 下午2:40
 * 有问题请联系 zhangyong7120180@163.com
 */
public class LuneneUtils {

    private static final Logger logger = Logger.getLogger(LuneneUtils.class);

    //======================================================== 常量定义  开始 ==================================================================

    public static final int OPT_ADD = 0x01;        //添加索引

    public static final int OPT_UPDATE = 0x02;    //更新索引

    public static final int OPT_DELETE = 0x04;    //删除索引

    private final static int MAX_COUNT = 1000;

    public static final String FN_ID = "LUNENE_ID";

    public final static String FN_CLASSNAME = "LUNENE_CLASSNAME";

    //======================================================== 常量定义  结束==================================================================

    private final static BooleanQuery nullQuery = new BooleanQuery();

    private final static IKAnalyzer analyzer = new IKAnalyzer();

    private static IndexWriter getWriter(Class<? extends Searchable> objClass) throws IOException {
        Directory dir = FSDirectory.open(new File(LuceneConfig.getPropertyValue(LuceneConfig.STORE_PATH) + File.separator + objClass.getSimpleName()));
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        return new IndexWriter(dir, config);
    }

    private static Document obj2Doc(Searchable searchable) {
        if (searchable == null) {
            return null;
        }

        Document document = new Document();
        document.add(new StringField(FN_ID, searchable.getId(), Field.Store.YES));
        document.add(new StringField(FN_CLASSNAME, searchable.getClass().getName(), Field.Store.YES));

        //存储字段
        List<String> fields = searchable.getStoreFields();
        if (fields != null) {
            for (String fn : fields) {
                Object fv = readField(searchable, fn);
                if (fv != null) {
                    document.add(obj2field(fn, fv, true));
                }
            }
        }

        //需要索引的字段
        List<String> indexFields = searchable.getIndexFields();
        if (indexFields != null) {
            for (String fn : indexFields) {
                String fv = (String) readField(searchable, fn);
                System.out.println("fn==" + fn + " fv==" + fv);
                if (fv != null) {
                    TextField tf = new TextField(fn, fv, Field.Store.NO);
                    tf.setBoost(searchable.boost());
                    document.add(tf);
                }
            }
        }

        return document;
    }

    private static Object readField(Object obj, String field) {
        try {
            return PropertyUtils.getProperty(obj, field);
        } catch (Exception e) {
            logger.error("Unabled to get property '" + field + "' of " + obj.getClass().getName(), e);
            return null;
        }
    }

    private static Field obj2field(String field, Object fieldValue, boolean store) {
        if (fieldValue == null) {
            return null;
        }
        if (fieldValue instanceof Date) { //日期
            return new LongField(field, ((Date) fieldValue).getTime(), store ? Field.Store.YES : Field.Store.NO);
        }
        if (fieldValue instanceof Number) { //其他数值
            return new StringField(field, String.valueOf(((Number) fieldValue).longValue()), store ? Field.Store.YES : Field.Store.NO);
        }
        //其他默认当字符串处理
        return new StringField(field, (String) fieldValue, store ? Field.Store.YES : Field.Store.NO);
    }

    public static void add(Searchable searchable) throws IOException {
        IndexWriter writer = getWriter(searchable.getClass());
        try {
            writer.addDocument(obj2Doc(searchable));
            writer.commit();
        } finally {
            writer.close();
            writer = null;
        }
    }

    public static void delete(Searchable searchable) throws IOException {
        IndexWriter writer = getWriter(searchable.getClass());
        try {
            writer.deleteDocuments(new Term(LuneneUtils.FN_ID, searchable.getId()));
            writer.commit();
        } finally {
            writer.close();
            writer = null;
        }
    }

    //===============================================================查询工具============================================================
    private static IndexSearcher getSearcher(Class<? extends Searchable> objClass) throws IOException {
        Directory dir = FSDirectory.open(new File(LuceneConfig.getPropertyValue(LuceneConfig.STORE_PATH) + File.separator + objClass.getSimpleName()));
        return new IndexSearcher(DirectoryReader.open(dir));
    }

    /**
     * 获取文档对应的对象类
     *
     * @param doc
     * @return
     * @throws ClassNotFoundException
     */
    public static Searchable doc2obj(Document doc) {
        try {
            String id = doc.get(FN_ID);
            if (StringUtils.isBlank(id)) {
                return null;
            }
            Searchable obj = (Searchable) Class.forName(doc.get(FN_CLASSNAME)).newInstance();
            obj.setId(id);
            return obj;
        } catch (Exception e) {
            logger.error("Unabled generate object from document#id=" + doc.toString(), e);
            return null;
        }
    }

    /**
     * 单库搜索
     *
     * @param objClass
     * @param query
     * @param filter
     * @param sort
     * @param page
     * @param count
     * @return
     * @throws IOException
     */
    public static List<String> find(Class<? extends Searchable> objClass, Query query, Filter filter, Sort sort, int page, int count) throws IOException {
        IndexSearcher searcher = getSearcher(objClass);
        List<Searchable> results = find(searcher, query, filter, sort, page, count);
        List<String> ids = new ArrayList<String>();
        for (Searchable obj : results) {
            if (obj != null) {
                ids.add(obj.getId());
            }
        }
        return ids;
    }

    /**
     * 搜索
     *
     * @param searcher
     * @param query
     * @param filter
     * @param sort
     * @param page
     * @param count
     * @return
     * @throws IOException
     */
    private static List<Searchable> find(IndexSearcher searcher, Query query, Filter filter, Sort sort, int page, int count) throws IOException {
        try {
            TopDocs hits = null;
            if (filter != null && sort != null) {
                hits = searcher.search(query, filter, MAX_COUNT, sort);
            } else if (filter != null) {
                hits = searcher.search(query, filter, MAX_COUNT);
            } else if (sort != null) {
                hits = searcher.search(query, MAX_COUNT, sort);
            } else {
                hits = searcher.search(query, MAX_COUNT);
            }
            if (hits == null) {
                return null;
            }
            List<Searchable> results = new ArrayList<Searchable>();
            int nBegin = (page - 1) * count;
            int nEnd = Math.min(nBegin + count, hits.scoreDocs.length);
            for (int i = nBegin; i < nEnd; i++) {
                ScoreDoc s_doc = (ScoreDoc) hits.scoreDocs[i];
                Document doc = searcher.doc(s_doc.doc);
                Searchable obj = doc2obj(doc);
                if (obj != null && !results.contains(obj)) {
                    results.add(obj);
                }
            }
            return results;
        } catch (IOException e) {
            logger.error("Unabled to find via query: " + query, e);
        }
        return null;
    }

    public static Query createQuery(String field, String q, float boost) {
        if (StringUtils.isBlank(q) || StringUtils.isBlank(field)) {
            return nullQuery;
        }
        QueryParser parser = new QueryParser(Version.LUCENE_40, field, analyzer);
        parser.setDefaultOperator(QueryParser.AND_OPERATOR);
        try {
            Query querySinger = parser.parse(q);
            querySinger.setBoost(boost);
            return querySinger;
        } catch (Exception e) {
            TermQuery queryTerm = new TermQuery(new Term(field, q));
            queryTerm.setBoost(boost);
            return queryTerm;
        }
    }

    /**
     * 根据查询条件统计搜索结果数
     *
     * @param searcher
     * @param query
     * @param filter
     * @return
     * @throws IOException
     */
    private static int count(IndexSearcher searcher, Query query, Filter filter) throws IOException {
        try {
            TotalHitCountCollector thcc = new TotalHitCountCollector();
            if (filter != null) {
                searcher.search(query, filter, thcc);
            } else {
                searcher.search(query, thcc);
            }
            return Math.min(MAX_COUNT, thcc.getTotalHits());
        } catch (IOException e) {
            logger.error("Unabled to find via query: " + query, e);
            return -1;
        }
    }

    /**
     * 多个资料库的搜索
     *
     * @param objClasses
     * @return
     * @throws IOException
     */
    private static IndexSearcher getSearchers(List<Class<? extends Searchable>> objClasses) throws IOException {
        IndexReader[] readers = new IndexReader[objClasses.size()];
        int idx = 0;
        for (Class<? extends Searchable> objClass : objClasses) {
            FSDirectory dir = FSDirectory.open(new File(LuceneConfig.getPropertyValue(LuceneConfig.STORE_PATH) + File.separator + objClass.getSimpleName()));
            readers[idx++] = DirectoryReader.open(dir);
        }
        return new IndexSearcher(new MultiReader(readers, true));
    }

    /**
     * 多库搜索
     *
     * @param objClasses
     * @param query
     * @param filter
     * @return
     * @throws IOException
     */
    public static int count(List<Class<? extends Searchable>> objClasses, Query query, Filter filter) throws IOException {
        IndexSearcher searcher = getSearchers(objClasses);
        return count(searcher, query, filter);
    }

    public static int count(Class<? extends Searchable> objClass, Query query, Filter filter) throws IOException {
        IndexSearcher searcher = getSearcher(objClass);
        return count(searcher, query, filter);
    }


}
