package com.srx.transaction.Entities.DTO;

import com.srx.transaction.Enum.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class ResultMessage implements Serializable {
    private ResultCode ResultCode;
    private List<Object> Result;
    private String message;
    private Integer code;

    public ResultMessage(ResultCode resultCode, Object... result) {
        ResultCode = resultCode;
        Result = new ArrayList<>();
        if (result.length == 0)
            Result.add(null);
        for (int i = 0; i < result.length; i++) {
            Result.add(result[i]);
        }
        this.message = this.ResultCode.getMessage();
        this.code = this.ResultCode.getCode();
    }
}
