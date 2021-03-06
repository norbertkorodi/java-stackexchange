package org.stackexchange.api.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stackexchange.api.constants.ApiConstants.Questions;
import org.stackexchange.api.constants.StackSite;

import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ApiUris {
    private static final String API_2_1 = "https://api.stackexchange.com/2.1";
    private static final String API_2_2 = "https://api.stackexchange.com/2.2";
//    private static final String SAMPLE_EXCERPT_SEARCH= "https://api.stackexchange.com/2.2/search/excerpts?page=StackOverflow&pagesize=10&fromdate=1451952000&todate=1451952000&order=desc&min=1451779200&max=1451779200&sort=activity&q=python&accepted=True&answers=yes&body=asdf&closed=False&migrated=True&notice=True&nottagged=asdf&tagged=tagged&title=title&user=asdf&url=asdf&views=views&wiki=True&site=stackoverflow";

    private static final Logger logger = LoggerFactory.getLogger(ApiUris.class);

    private ApiUris() {
        throw new AssertionError();
    }

    // API

    public static String getQuestionsUri(final int min, final StackSite site) {
        return getQuestionsUri(min, site, 1);
    }

    public static String getQuestionsUri(final int min, final StackSite site, final int page) {
        return buildQuestionsMultipleUriParams(min, site, Questions.S_QUESTIONS, page);
    }

    public static String getSingleQuestionUri(final StackSite site, final long id) {
        final String operation = Questions.S_QUESTIONS + "/" + id;
        return getSingleUri(site, operation);
    }

    public static String getTagUri(final int min, final StackSite site, final String tag) {
        return getTagUri(min, site, tag, 1);
    }

    public static String getTagUri(final int min, final StackSite site, final String tag, final int page) {
        return buildQuestionsMultipleUriParams(min, site, Questions.S_TAGS_S + tag + Questions.S_FAQ, page);
    }

    static String buildQuestionsMultipleUriParams(final int min, final StackSite site, final String operation, final int page) {
        Map<String, ? extends Serializable> uriParams = Collections.unmodifiableMap(Stream.of(
                new SimpleEntry<>(Questions.ORDER, Questions.DESC),
                new SimpleEntry<>(Questions.SORT, Questions.VOTES),
                new SimpleEntry<>(Questions.MIN, min),
                new SimpleEntry<>(Questions.SITE, site),
                new SimpleEntry<>(Questions.PAGE, page))
                .collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue)));
        return buildURIString(operation, uriParams);
    }

    private static String buildURIString(String operation, Map<String, ? extends Serializable> uriParams) {
        final String params = buildParamsString(uriParams);
        return getApiString() + operation + params;
    }

    private static String buildParamsString(Map<String, ? extends Serializable> uriParamKeyValue) {
        RequestBuilder requestBuilder = new RequestBuilder();
        uriParamKeyValue.entrySet().stream().forEach(e -> requestBuilder.add(e.getKey(), e.getValue()));
        return requestBuilder.build();
    }

    private static String getApiString() {
        return API_2_2;
    }

    static String getSingleUri(final StackSite site, final String operation) {
        final String params = new RequestBuilder().add(Questions.SITE, site).build();
        return getApiString() + operation + params;
    }

}
