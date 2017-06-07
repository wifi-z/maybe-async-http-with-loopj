# maybe-async-http
android async http client

## 파싱된 데이터를 저장하는 클래스
<pre>
public class TestData {
    @JsonProperty("member")
    private ArrayList<Member> status;

    public String getStatus() {
        return status.get(0).name;
    }


    public static class Member {
        @JsonProperty("custName")
        private String name;
    }
}
</pre>

## api 주소 구성 enum 구현
<pre>
public enum Api implements Protocol {
    TEST("url");

    String url;

    Api(String url) {
        this.url = url;
    }

    @Override
    public String getUrl() {
        return url;
    }
}
</pre>

## 응답 클래스 
<pre>
public abstract class JsonResponse implements JsonAsyncHttpResponse {
    @Override
    public void onSuccess(RequestToJson requestData) {
        onCompleted(requestData);
    }

    @Override
    public void onFailure(RequestToJson requestData) {
        onCompleted(requestData);
    }

    @Override
    public void onProgress(long size, long totalSize, float percent) {

    }

    @Override
    public abstract void onCompleted(RequestToJson requestData);
}
</pre>

## 단일 리퀘스트
<pre>
private void singleRequest() {
    RequestToJson request = new RequestToJson.Builder(Api.TEST, HttpMethod.GET, TestData.class, new JsonResponse() {
        @Override
        public void onCompleted(RequestToJson requestData) {
            TestData response = (TestData) requestData.getDeserializeObject();
      
        }
    }).addParameter("key", "value").build();

    AsyncHttp asyncHttp = new AsyncHttp();
    asyncHttp.sendRequest(request);
}
</pre>

## 비동기 멀티 리퀘스트 - 리퀘스트간 순서가 상관 없다면..
<pre>
private void asyncMultiRequest() {
    RequestToJson request1 = new RequestToJson.Builder(
            Api.TEST,
            HttpMethod.GET, TestData.class, new JsonResponse() {
        @Override
        public void onCompleted(RequestToJson requestData) {
        }
    }).addParameter("key", "value").build();

    RequestToJson request2 = new RequestToJson.Builder(
            Api.TEST,
            HttpMethod.GET, TestData.class, new JsonResponse() {
        @Override
        public void onCompleted(RequestToJson requestData) {
        }
    }).addParameter("key", "value").build();

    AsyncHttp asyncHttp = new AsyncHttp();
    AsyncRequests asyncRequests = new AsyncRequests.Builder(asyncHttp, new RequestsResponse() {
        @Override
        public void requestFinished() {
        }
    }).addRequest(request1).addRequest(request2).build();

    asyncRequests.sendRequest();
}
</pre>

## 동기 멀티 리퀘스트 - 리퀘스트간 순서가 보장되어야 한다면..
<pre>
private void syncMultiRequest() {
    RequestToJson request1 = new RequestToJson.Builder(
            Api.TEST,
            HttpMethod.GET, TestData.class, new JsonResponse() {
        @Override
        public void onCompleted(RequestToJson requestData) {
        }
    }).addParameter("key", "value").build();

    RequestToJson request2 = new RequestToJson.Builder(
            Api.TEST,
            HttpMethod.GET, TestData.class, new JsonResponse() {
        @Override
        public void onCompleted(RequestToJson requestData) {
        }
    }).addParameter("key", "value").build();

    AsyncHttp asyncHttp = new AsyncHttp();
    SyncRequests syncRequests = new SyncRequests.Builder(asyncHttp, new RequestsResponse() {
        @Override
        public void requestFinished() {
        }
    }).addRequest(request1).addRequest(request2).build();

    syncRequests.sendRequest();
}
</pre>

#### Thanks
http://loopj.com/android-async-http/ - Android Async Http Client.  
https://github.com/FasterXML - jackson.  

#### License

    Copyright wifi-z

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
