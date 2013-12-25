package ik;

import com.diyicai.bean.TrasDailyInfo;
import com.diyicai.util.LuneneUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhangyong
 * Date: 13-12-21
 * Time: 下午4:00
 * any questions ,please contact zhangyong7120180@163.com
 */
public class IKAnalyzerDemo2 {

    public static void main(String[] args) throws ParseException, IOException {
        Analyzer analyzer = new IKAnalyzer();

        String keyword = "12102320424206207831";
        //String fieldName = "trasContent";
        String fieldName = LuneneUtils.FN_ID;
        QueryParser qp = new QueryParser(Version.LUCENE_40, fieldName, analyzer);
        qp.setDefaultOperator(QueryParser.AND_OPERATOR);

        Query query = qp.parse(keyword);

        List<String> list = LuneneUtils.find(TrasDailyInfo.class, query, null, null, 1, 100);
        System.out.println(list.size());
        System.out.println(list);

    }

}
