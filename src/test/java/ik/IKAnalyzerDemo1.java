package ik;

import com.diyicai.bean.TrasDailyInfo;
import com.diyicai.util.LuceneConfig;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: zhangyong
 * Date: 13-12-21
 * Time: 下午4:00
 * any questions ,please contact zhangyong7120180@163.com
 */
public class IKAnalyzerDemo1 {

    public static void main(String[] args) {
        String fieldName = "LUNENE_ID";
        Analyzer analyzer = new IKAnalyzer();
        Directory directory = null;
        IndexReader ireader = null;
        IndexSearcher indexSearcher = null;
        try {
            String path = LuceneConfig.getPropertyValue(LuceneConfig.STORE_PATH) + File.separator + TrasDailyInfo.class.getSimpleName();
            System.out.println("path==" + path);
            directory = FSDirectory.open(new File(LuceneConfig.getPropertyValue(LuceneConfig.STORE_PATH) + File.separator + TrasDailyInfo.class.getSimpleName()));

            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);

            //============================ 搜索过程 =================================================
            indexSearcher = new IndexSearcher(DirectoryReader.open(directory));

            String keyword = "1";
            QueryParser qp = new QueryParser(Version.LUCENE_40, fieldName, analyzer);
            qp.setDefaultOperator(QueryParser.AND_OPERATOR);

            Query query = qp.parse(keyword);

            TopDocs topDocs = indexSearcher.search(query, 5);
            System.out.println("命中:" + topDocs.totalHits);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ireader != null) {
                try {
                    ireader.close();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
            if (directory != null) {
                try {
                    directory.close();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }

    }

}
