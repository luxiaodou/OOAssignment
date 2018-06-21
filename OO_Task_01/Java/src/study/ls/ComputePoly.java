package study.ls;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by luxia on 2016/3/2.
 */
class ComputePoly {
    private Poly polylist[] = new Poly[100000000];
    private Operator opList[] = new Operator[100000000];
    private int pindex = 0, oindex = 0;

    enum Operator {ADD, SUB}

    public ComputePoly() {
    }

    public void buildPoly() throws IOException, FormatException {
        int temp = 0;
        int flag = 0;
        long coffe = 0;
        int exp = 0;
        int flagbtnnum = 0;  //数字中间夹着符号视为非法
        int flagnxtterm = 0;
        int sign = 1;
        int pflag = 0;
        int nflag = 0;
        int ch;
        Poly p1 = new Poly();
        Poly p2 = new Poly();

        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));

        while ((ch = bf.read()) != -1 || ch != '\n' || ch != 10)
        {
            if(ch == 10 || ch == '\n')
                break;
            if (ch == ' ' || ch == '{' || ch == '\t')
            {
                if (ch == ' ' || ch == '\t') continue;
                if (ch == '{')
                {
                    nflag = 1;
                    sign = 1;
                    pflag = 0;
                    p1 = new Poly();
                    while ((ch = bf.read()) != '}')
                    {
                        if (ch == ' ' || ch == '\t')
                            continue;
                        else if (ch == '(')
                        {
                            flagnxtterm = 0;
                            flagbtnnum =0;
                            pflag = 1;
                            while ((ch = bf.read()) != ')')
                            {
                                if (ch == ' ' || ch == '\t') continue;
                                else if (flag >= 2)
                                {
                                    throw new FormatException("逗号使用错误！");
                                }
                                else if (ch >= '0' && ch <= '9')
                                {
                                    temp = 10 * temp + ch - '0';
                                    flagbtnnum = 1;
                                }
                                else if (ch == ',')
                                {
                                    if (flag == 0){
                                        coffe = (sign == 1) ? temp : -temp;
                                        flagbtnnum = 0;
                                    }
                                    else throw new FormatException("逗号使用错误！");
                                    flag++;
                                    temp = 0;
                                    sign = 1;
                                }
                                else if ((ch == '+' || ch == '-') && flagbtnnum == 0)
                                {
                                    if(ch == '+')
                                    {
                                        sign = sign;
                                    }
                                    if(ch == '-')
                                    {
                                        sign = -sign;
                                    }
                                }
                                else throw new FormatException("输入格式异常！错误可能出现在“()”中！");
                            }

                            if(flag == 1 && sign != -1)
                            {
                                exp = temp;
                                temp = 0;
                            }
                            else throw new FormatException("指数输入错误！指数应为非负整数！");
                            sign = 1; //出括号恢复默认正号
                            flag = 0;
                            temp = 0;
                            p2 = new Poly(coffe, exp);
                            coffe = 0;
                            exp = 0;
                        }
                        else if (ch == ',')
                        {
                            p1 = p1.add(p2);
                            flagnxtterm = 1;
                        }
                        else throw new FormatException("输入格式异常！错误可能出现在“{}”中！");
                    }
                }
                if (ch == '}' && pflag == 1 && flagnxtterm == 0)
                {
                    p1 = p1.add(p2);
                    polylist[pindex++] = p1;
                }
                else
                {
                    p2 = new Poly();
                    p1 = p1.add(p2);
                    polylist[pindex++] = p1;
                }
            }
            if (ch == '+')
            {
                opList[oindex++] = Operator.ADD;
            }
            if (ch == '-')
            {
                opList[oindex++] = Operator.SUB;
            }
            if((ch == '+' || ch == '-') && nflag == 0){
                throw new FormatException("请不要在第一个大括号前添加非空格符号！");
            }
        }
    }

    void compute() {
        Poly p = polylist[0];
        Poly p1 = new Poly();
        Poly p2;
        if(polylist[1] != null)
            for (int i = 1;  polylist[i] != null; i++) {
                p2 = polylist[i];
                Operator op = opList[i - 1];
                if (op == Operator.ADD)
                    try {
                        p1 = p.add(p2);
                    } catch (FormatException e) {
                        e.printStackTrace();
                    }
                if (op == Operator.SUB)
                    try {
                        p1 = p.sub(p2);
                    } catch (FormatException e) {
                        e.printStackTrace();
                    }
                p = p1;
            }
        p.info();
    }
}
