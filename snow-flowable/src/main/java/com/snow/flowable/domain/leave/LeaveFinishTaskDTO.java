package com.snow.flowable.domain.leave;

import com.snow.flowable.domain.FinishTaskDTO;
import com.snow.system.domain.SysOaLeave;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2020-11-23 22:13
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveFinishTaskDTO  extends FinishTaskDTO implements Serializable {

    private static final long serialVersionUID = -6573836100426806321L;
    /**
     * hr userID
     */
    private String hr;

}
