package com.example.demo.common;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.passay.*;

import java.util.Arrays;
import java.util.List;

/**
 * 类功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-17
 */
public class PasswordConstraintValidator  implements ConstraintValidator<ValidPassword,String> {
    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        PasswordValidator validator = new PasswordValidator(Arrays.asList(
                //长度规则，8-30
                new LengthRule(8,30),
                //字符规则 至少有一个大写字母
                new CharacterRule(EnglishCharacterData.UpperCase,1),
                //字符规则 至少有一个小写字母
                new CharacterRule(EnglishCharacterData.LowerCase,1),
                //字符规则 至少有一个特殊字符
                new CharacterRule(EnglishCharacterData.Special,1),
                //非法顺序规则 不允许有5个连续字母表顺序的字母，比如不允许abcde
                new IllegalSequenceRule(EnglishSequenceData.Alphabetical,5,false),
                //非法顺序规则 不允许有5个连续数字顺序的数字 比如不允许12345
                new IllegalSequenceRule(EnglishSequenceData.Numerical,5,false),
                //非法顺序规则 不允许有5个连续键盘顺序的字母 比如不允许asdfg
                new IllegalSequenceRule(EnglishSequenceData.USQwerty,5,false),
                //空格规则,不能有空格
                new WhitespaceRule()
        ));
        return validator.validate(new PasswordData(s)).isValid();
    }
}