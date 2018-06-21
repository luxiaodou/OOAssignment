#include <stdio.h>
#include <stdlib.h>
#define MAX 100000

typedef struct Poly
{
    int cofee[MAX];
}PNode,*PLink;

void errdisplay()
{
    printf(" ‰»Î”–ŒÛ£°");
    return 0;
}

void Add(PLink p, PLink q)
{
    int i;
    for(i = 0;i < MAX; i++)
        p->cofee[i] += q->cofee[i];
}

void Sub(PLink p, PLink q)
{
    int i;
    for(i = 0; i< MAX; i++)
        p->cofee[i] -= q->cofee[i];
}

void Prin(PLink head)
{
    int i;
    for(i=0;i<MAX;i++){
        if(head->cofee[i] != 0){
            if(i == 0)
                printf("%d",head->cofee[0]);
            else
                printf("%dx^%d",head->cofee[i],i);
        }
    }
}


int main()
{
    int state[2],flagn,flagp,flagcomma,flago,flag,flagf;
    char ch,lastCh,op;
    PNode head,p1;
    flagn = 0;
	flag = 0;
	op = '+';
	flago = 1;
	lastCh = '+';

    memset(head.cofee,0,sizeof(head.cofee));
    while(scanf("%c",&ch) == 1) {
		if(ch == ' ') {
			continue;
		} else if(ch == '\n') {
			goto FINISH;
		} else if(ch == '{') {
			if(lastCh != '+' && lastCh != '-') errdisplay();
			flag = 1;
			memset(p1.cofee,0,sizeof(p1.cofee));
			lastCh = ch;
			while(scanf("%c",&ch) == 1) {
				if(ch == ' ') {
					continue;
				} else if(ch == '\n'){
					if(flago > 0) errdisplay();
					if(flag > 0) errdisplay();
					if(flagn > 0) errdisplay();
					if(flagcomma > 0) errdisplay();
					goto FINISH;
				} else if(ch == '{') {
					errdisplay();
				} else if(ch == '}') {
					if(lastCh != ')' && lastCh != '{') errdisplay();
					if(flagn > 0) errdisplay();
					if(flagcomma > 0) errdisplay();
					flag = 0;
					if(op == '+')
						Add(&head,&p1);
					else if(op == '-')
						Sub(&head,&p1);
					else errdisplay();
					flago == 0;
					break;
				} else if(ch == '(') {
					if(lastCh != ',' && lastCh != '{') errdisplay();
					if(flagn > 0) errdisplay();
					flagcomma = 0;
					flagn = 1;
					flagp = 0;
					state[0] = state[1] = 0;
					flagf = 1;
				} else if('0' <= ch && ch <= '9') {
					if(flagn == 0) errdisplay();
					if(lastCh != '(' && lastCh != ',' && (lastCh<'0' || lastCh > '9') && lastCh != '+' && lastCh != '-') errdisplay(1);
					state[flagp] *= 10;
					state[flagp] += ch - '0';
				} else if(ch == ',') {
					if(lastCh != ')' && (lastCh < '0' || lastCh > '9')) errdisplay(1);
					if(flagcomma > 0) errdisplay();
					if('0' <= lastCh && lastCh <= '9')
					state[flagp] *= flagf;
					flagcomma++;
					flagp = 1;
					flagf = 1;
				} else if(ch == ')') {

					if(flagcomma > 1) errdisplay();
					flagcomma = 0;
					if(lastCh != '(' && (lastCh < '0' || lastCh > '9')) errdisplay(1);
					if(flagn == 0) errdisplay();
					flagn = 0;
					p1.cofee[state[1]] = state[0];
				} else if(ch == '-' || ch == '+') {
					if(flagn == 0) errdisplay();
					if(lastCh != '(' && lastCh != ',') errdisplay();
					if(ch == '-') flagf = -1;
				}
				lastCh = ch;
			}
		} else if(ch == '+' || ch == '-') {
			if(lastCh != '}') errdisplay();
			if(flago == 1) errdisplay();
			flago = 1;
			op = ch;
		} else {
			errdisplay();
		}
		lastCh = ch;
	}
	FINISH:
    Prin(&head);
    return 0;
}
