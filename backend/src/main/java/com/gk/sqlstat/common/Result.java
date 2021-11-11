package com.gk.sqlstat.common;

import com.alibaba.fastjson.JSON;

/**
 * 根据不同的参数需要，返回不通的result对象<br>
 * 方便对DataResult、StatusResult等的调用
 *
 * @param <T> 数据类型
 * @author LIZ
 * @version 1.0
 */
public class Result<T> {

    /**
     * @param resultOption
     * @param data
     * @return
     */
    public static <T> IResult data(Option resultOption, T data) {
        return DataResult.of(resultOption, data);
    }

    public static <T> IResult dataSuccess(T data) {
        return DataResult.of(Option.SUCCESS, data);
    }

    public static IResult status(Option resultOption) {
        return StatusResult.of(resultOption);
    }

    public static IResult success() {
        return StatusResult.of(Option.SUCCESS);
    }

    public static IResult success(String msg) {
        return StatusResult.of(Option.forSuccess(msg));
    }

    public static IResult fail() {
        return StatusResult.of(Option.FAIL);
    }

    public static IResult fail(String msg) {
        return StatusResult.of(Option.forFail(msg));
    }


    public static void main(String[] args) {
        System.out.println(JSON.toJSONString(Result.status(Option.SUCCESS)));
    }

    public static enum Option {

        SUCCESS(0, "SUCCESS"), FAIL(-1, "FAIL");

        private int value = 0;
        private String msg = "SUCCESS";

        private Option(int value, String msg) {
            this.value = value;
            this.msg = msg;
        }

        /**
         * 更新成功的提示语句
         *
         * @param msg
         * @return
         */
        public static Option forSuccess(String msg) {
            Option.SUCCESS.setMsg(msg);
            return Option.SUCCESS;
        }

        /**
         * 重新设置失败的提示语句
         *
         * @param msg
         * @return
         */
        public static Option forFail(String msg) {
            Option.FAIL.setMsg(msg);
            return Option.FAIL;
        }

        public int getValue() {
            return value;
        }

        public String getMsg() {
            return msg;
        }

        public Option setMsg(String msg) {
            this.msg = msg;
            return this;
        }
    }
}
