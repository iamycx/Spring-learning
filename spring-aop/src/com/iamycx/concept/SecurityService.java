package com.iamycx.concept;

public interface SecurityService {
    boolean checkAccess(User user,String service);
}
