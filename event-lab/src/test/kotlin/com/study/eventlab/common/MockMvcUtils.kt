package com.study.eventlab.common

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

class MockMvcUtils {
    companion object {
        fun mockMvc(context: WebApplicationContext, restDocumentation: RestDocumentationContextProvider?): MockMvc =
            MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters<DefaultMockMvcBuilder>(
                    Filter {
                        request: ServletRequest, response: ServletResponse, chain: FilterChain ->
                            response.characterEncoding = Charsets.UTF_8.name()
                            chain.doFilter(request, response)
                    }
                ).apply<DefaultMockMvcBuilder>(
                    MockMvcRestDocumentation
                        .documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(
                            Preprocessors.prettyPrint(),
                            Preprocessors.removeHeaders(HttpHeaders.CONTENT_LENGTH, HttpHeaders.HOST),
                        ).withRequestDefaults(
                            Preprocessors.prettyPrint(),
                            Preprocessors.removeHeaders(HttpHeaders.PRAGMA, HttpHeaders.EXPIRES, HttpHeaders.CONTENT_LENGTH, HttpHeaders.CACHE_CONTROL),
                            Preprocessors.removeMatchingHeaders("X-(.*)$")
                        ),
                )
                .build()


    }
}