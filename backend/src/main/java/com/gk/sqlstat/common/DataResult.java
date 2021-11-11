package com.gk.sqlstat.common;


/**
 * 数据结果,状态基础上增加返回数据 <br>
 * 主要用于向调用接口返回数据结果<br>
 * 如果只关注调用状态,不关心调用结果可使用StatusResult
 *
 * @param <T>
 * @author LIZ
 * @version 1.0
 */
class DataResult<T> extends StatusResult {

    private T data;

    private DataResult(Result.Option resultOption) {
        super(resultOption);
    }

    public static <T> IResult of(Result.Option resultOption, final T data) {
        DataResult<T> dr = new DataResult<>(resultOption);
        dr.data = data;
        return dr;
    }

    public T getData() {
        return data;
    }

}
