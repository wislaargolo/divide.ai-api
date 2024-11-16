package com.ufrn.imd.divide.ai.service.interfaces;

import java.util.Map;

public interface IHtmlTemplateService {
    String loadHtmlTemplate(String templateName, Map<String, String> placeholders);
}
