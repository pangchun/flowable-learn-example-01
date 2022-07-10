package cn.pangchun.flowable;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class SendRejectionMail implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("请假被拒绝,,,安心工作吧");
    }
}
