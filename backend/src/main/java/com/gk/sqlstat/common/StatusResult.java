package com.gk.sqlstat.common;


/**
 * 状态结果<br>
 * 主要用于调用接口查看调用状态，不关注数据<br>
 * 如果关注数据，请使用DataResult
 *
 * @author LIZ
 * @version 1.0
 */
class StatusResult implements IResult {

    private int status;
    private String msg;

    public StatusResult(Result.Option resultOption) {
        this.status = resultOption.getValue();
        this.msg = resultOption.getMsg();
    }

    public static final IResult of(Result.Option resultOption) {
        return new StatusResult(resultOption);
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public boolean isSuccess() {
        return status == 0;
    }


}
