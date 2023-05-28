package com.criticaltechworks.pelsoczi.data.serialization

import com.criticaltechworks.pelsoczi.util.encode
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.putJsonArray


// region: error json

val errorJson = buildJsonObject {
    put("status", encode("error"))
    put("code", encode("apiKeyMissing"))
    put("message", encode("Your API key is missing."))
}

// endregion

// region: complete json

val sourceJson = buildJsonObject {
    put("id", encode("bbc-news"))
    put("name", encode("BBC News"))
}
val articleJson = buildJsonObject {
    put("source", sourceJson)
    put("author", encode("BBC News"))
    put("title", encode("China's C919 passenger plane makes maiden flight"))
    put("description", encode("China hopes the C919 will end the dominance of Airbus and Boeing - but it relies on Western components."))
    put("url", encode("http://www.bbc.co.uk/news/world-asia-china-65737081"))
    put("urlToImage", encode("https://ichef.bbci.co.uk/news/1024/branded_news/126F5/production/_129890557_0c36b96325745e2c5ab3d9927a594cbd5d7c18e7-1.jpg"))
    put("publishedAt", encode("2023-05-28T04:52:18.0364993Z"))
    put("content", encode("China's first domestically produced passenger jet has taken off on its maiden commercial flight. \\r\\nState TV showed the C919 rising into the skies above Shanghai, heading to the capital Beijing early … [+960 chars]"))
}
val successJson = buildJsonObject {
    put("status", encode("ok"))
    put("totalResults", encode("1"))
    putJsonArray("articles") {
        add(articleJson)
    }
}

// endregion

// region: null data definitions

val sourceNullsJson = buildJsonObject {
    put("id", encode(null))
    put("name", encode("BBC News"))
}
val articleNullsJson = buildJsonObject {
    put("source", sourceNullsJson)
    put("author", encode(null))
    put("title", encode("China's C919 passenger plane makes maiden flight"))
    put("description", encode(null))
    put("url", encode("http://www.bbc.co.uk/news/world-asia-china-65737081"))
    put("urlToImage", encode(null))
    put("publishedAt", encode("2023-05-28T04:52:18.0364993Z"))
    put("content", encode(null))
}
val successNullsJson = buildJsonObject {
    put("status", encode("ok"))
    put("totalResults", encode("1"))
    putJsonArray("articles") {
        add(articleNullsJson)
    }
}

// endregion