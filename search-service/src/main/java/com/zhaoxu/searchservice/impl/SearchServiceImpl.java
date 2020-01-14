package com.zhaoxu.searchservice.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhaoxu.bean.PmsSearchParam;
import com.zhaoxu.bean.PmsSearchSkuInfo;
import com.zhaoxu.bean.PmsSkuAttrValue;
import com.zhaoxu.service.SearchService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.apache.http.util.TextUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import javax.swing.text.Highlighter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    JestClient jestClient;

    @Override
    public List<PmsSearchSkuInfo> list(PmsSearchParam pmsSearchParam) {

        String dsl = getSearchDSL(pmsSearchParam);

        Search search = new Search.Builder(dsl).addIndex("gmall0105").addType("PmsSkuInfo").build();

        SearchResult result = null;

        List<PmsSearchSkuInfo> pmsSearchSkuInfos = new ArrayList<>();

        try {
            result = jestClient.execute(search);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<SearchResult.Hit<PmsSearchSkuInfo, Void>> hits = result.getHits(PmsSearchSkuInfo.class);
        for (SearchResult.Hit<PmsSearchSkuInfo, Void> hit : hits) {
            PmsSearchSkuInfo source = hit.source;
            Map<String, List<String>> highlight = hit.highlight;
            if (highlight!=null) {
                String skuName = highlight.get("skuName").get(0);
                source.setSkuName(skuName);
            }
            pmsSearchSkuInfos.add(source);
        }

        return pmsSearchSkuInfos;
    }

    private String getSearchDSL(PmsSearchParam pmsSearchParam) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        List<PmsSkuAttrValue> skuAttrValueList = pmsSearchParam.getSkuAttrValueList();
        String keyword = pmsSearchParam.getKeyword();
        String catalog3Id = pmsSearchParam.getCatalog3Id();

        if (!TextUtils.isEmpty(catalog3Id)) {
            TermQueryBuilder termQueryBuilder = new TermQueryBuilder("catalog3Id", catalog3Id);
            boolQueryBuilder.filter(termQueryBuilder);
        }

        if (skuAttrValueList != null) {
            for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
                TermQueryBuilder termQueryBuilder = new TermQueryBuilder("skuAttrValueList.valueId",pmsSkuAttrValue.getValueId());
                boolQueryBuilder.filter(termQueryBuilder);
            }
        }

        if (!TextUtils.isEmpty(keyword)) {
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName",keyword);
            boolQueryBuilder.must(matchQueryBuilder);
        }

        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(20);
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<span style='color:red;'>");
        highlightBuilder.field("skuName");
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder);

//        TermsAggregationBuilder groupby_attr = AggregationBuilders.terms("groupby_attr").field("skuAttrValueList.valueId");
//        searchSourceBuilder.aggregation(groupby_attr);

        searchSourceBuilder.sort("id", SortOrder.DESC);
        String searchDSL = searchSourceBuilder.toString();
        return searchDSL;
    }


}
