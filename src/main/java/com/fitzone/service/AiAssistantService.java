package com.fitzone.service;

import com.fitzone.model.AiDietPlanRequest;
import com.fitzone.model.AiDietPlanResponse;
import com.fitzone.model.Member;
import com.fitzone.model.MemberBmr;

public interface AiAssistantService {
    AiDietPlanResponse generateDietPlan(Member member, MemberBmr bmr, AiDietPlanRequest request);
}
