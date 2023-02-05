package com.arguvos.yourtopwords.reversocontext;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QueryServiceBody {
    private String sourceLang;
    private String targetLang;
    @Builder.Default
    private int mode = 0;
    @Builder.Default
    private int npage = 1;
    private String sourceText;
    @Builder.Default
    private String targetText = "";
}
