package com.android.loginwithgmail;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

    public class AllRequest extends StringRequest{
        private static final String REGISTER_REQUEST_URL ="http://www.bangkokonholiday.com/Allhelp.php";
        private Map<String, String> params;
        public AllRequest(String type, Response.Listener<String> listener) {
            super(Request.Method.POST, REGISTER_REQUEST_URL, listener, null);
            params = new HashMap<>();
            params.put("type", type);
        }
        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }

