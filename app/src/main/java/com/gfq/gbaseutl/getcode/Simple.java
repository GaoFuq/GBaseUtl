package com.gfq.gbaseutl.getcode;

public class Simple {
    private GetCodeUtil getCodeUtil;

    private void getCode() {
        if (getCodeUtil == null) {
            getCodeUtil = new GetCodeUtil();
        }
//        phone = binding.editPhone.getText().toString();
//        getCodeUtil.getCode(phone, Constants.SCENE_REGISTER, binding.tvGetCode, getLifecycle());
    }
}
