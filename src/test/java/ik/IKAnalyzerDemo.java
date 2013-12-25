package ik;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: zhangyong
 * Date: 13-12-21
 * Time: 下午4:00
 * any questions ,please contact zhangyong7120180@163.com
 */
public class IKAnalyzerDemo {

    public static void main(String[] args) {
        String fieldName = "text";

        String text = "IK Analyzer是一种 它使用的全新的正向";

        Analyzer analyzer = new IKAnalyzer();
        Directory directory = null;
        IndexWriter iwriter = null;
        IndexReader ireader = null;
        IndexSearcher indexSearcher = null;

        try {
            directory = new RAMDirectory();

            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            iwriter = new IndexWriter(directory, config);

            Document doc = new Document();
            doc.add(new org.apache.lucene.document.Field("ID", "10000", org.apache.lucene.document.Field.Store.YES, org.apache.lucene.document.Field.Index.NOT_ANALYZED));
            doc.add(new org.apache.lucene.document.Field(fieldName, text, org.apache.lucene.document.Field.Store.YES, org.apache.lucene.document.Field.Index.ANALYZED));
            iwriter.addDocument(doc);
            iwriter.commit();

            //============================ 搜索过程 =================================================
            ireader = IndexReader.open(directory);

            indexSearcher = new IndexSearcher(ireader);

            String keyword = "正向";
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
