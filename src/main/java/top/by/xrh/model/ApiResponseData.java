package top.by.xrh.model;

/**
 * <p>Title: ApiResponseData</p>
 * <p>Description: 响应体封装</p>
 *
 * @author zwp
 * @date 2019/1/8 9:48
 */
public class ApiResponseData<T> {

    // 响应状态码(http请求方式)
    private int code;

    // 错误描述
    private String message;

    // 响应数据
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
