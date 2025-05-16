package com.study.eventlab.config

import org.slf4j.LoggerFactory
import org.slf4j.Logger

inline fun <reified T: Any> T.logger(): Logger = LoggerFactory.getLogger(T::class.java)
