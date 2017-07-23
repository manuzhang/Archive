#include "fix_point.h"
#include "lib/debug.h"


int n2fix(int n)
{
  return n * my_pow(2,FORMAT);
}

int fix2n_0(int fix)
{
  return fix / my_pow(2,FORMAT);
}

int fix2n_near(int fix)
{  
  return (fix >= 0) ? ((fix + my_pow (2,FORMAT - 1)) / my_pow (2,FORMAT)) : ((fix - my_pow (2,FORMAT - 1)) / my_pow (2,FORMAT));
}

int add2fix(int x, int y)
{
  return x + y;
}

int subtract2fix(int x, int y)
{
  return x - y;
}

int add_fix_n(int fix, int n)
{
  return fix + n * my_pow(2, FORMAT);
}

int subtract_fix_n(int fix, int n)
{
  return fix - n * my_pow(2, FORMAT);
}

int multiply2fix(int x, int y)
{
  return ((int64_t)x) * y / my_pow(2, FORMAT);
}

int multiply_fix_n(int fix, int n)
{
  return fix * n;
}

int divide2fix(int x, int y)
{
  ASSERT(y != 0);
  return ((int64_t)x) * my_pow(2, FORMAT) / y;
}

int divide_fix_by_n(int fix, int n)
{
  ASSERT(n != 0);
  return fix / n;
}

int my_pow(int n, int expo)
{
  int i;
  int result = 1;
  
  if (n != 0)
      for (i = 0; i <= expo - 1; i++)
         result *= n;
  return result;
}
