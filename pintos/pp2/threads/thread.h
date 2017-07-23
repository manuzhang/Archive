#ifndef THREADS_THREAD_H
#define THREADS_THREAD_H

#include <debug.h>
#include <hash.h>
#include <list.h>
#include <stdint.h>
#include <stdbool.h>
#include "filesys/file.h"
#include "threads/synch.h"

/* States in a thread's life cycle. */
enum thread_status
  {
    THREAD_RUNNING,     /* Running thread. */
    THREAD_READY,       /* Not running but ready to run. */
    THREAD_BLOCKED,     /* Waiting for an event to trigger. */
    THREAD_DYING        /* About to be destroyed. */
  };

/* Thread identifier type.
   You can redefine this to whatever type you like. */
typedef int tid_t;
#define TID_ERROR ((tid_t) -1)          /* Error value for tid_t. */

/* Thread priorities. */
#define PRI_MIN 0                       /* Lowest priority. */
#define PRI_DEFAULT 31                  /* Default priority. */
#define PRI_MAX 63                      /* Highest priority. */

#ifdef USERPROG

#define NAME_LENGTH 100
#define OPEN_MAX 128

#endif

/* A kernel thread or user process.

   Each thread structure is stored in its own 4 kB page.  The
   thread structure itself sits at the very bottom of the page
   (at offset 0).  The rest of the page is reserved for the
   thread's kernel stack, which grows downward from the top of
   the page (at offset 4 kB).  Here's an illustration:

   4 kB +---------------------------------+
   |          kernel stack           |
   |                |                |
   |                |                |
   |                V                |
   |         grows downward          |
   |                                 |
   |                                 |
   |                                 |
   |                                 |
   |                                 |
   |                                 |
   |                                 |
   |                                 |
   +---------------------------------+
   |              magic              |
   |                :                |
   |                :                |
   |               name              |
   |              status             |
   0 kB +---------------------------------+

   The upshot of this is twofold:

   1. First, `struct thread' must not be allowed to grow too
   big.  If it does, then there will not be enough room for
   the kernel stack.  Our base `struct thread' is only a
   few bytes in size.  It probably should stay well under 1
   kB.

   2. Second, kernel stacks must not be allowed to grow too
   large.  If a stack overflows, it will corrupt the thread
   state.  Thus, kernel functions should not allocate large
   structures or arrays as non-static local variables.  Use
   dynamic allocation with malloc() or palloc_get_page()
   instead.

   The first symptom of either of these problems will probably be
   an assertion failure in thread_current(), which checks that
   the `magic' member of the running thread's `struct thread' is
   set to THREAD_MAGIC.  Stack overflow will normally change this
   value, triggering the assertion. */
/* The `elem' member has a dual purpose.  It can be an element in
   the run queue (thread.c), or it can be an element in a
   semaphore wait list (synch.c).  It can be used these two ways
   only because they are mutually exclusive: only a thread in the
   ready state is on the run queue, whereas only a thread in the
   blocked state is on a semaphore wait list. */
struct thread
{
  /* Owned by thread.c. */
  tid_t tid;                          /* Thread identifier. */
  enum thread_status status;          /* Thread state. */
  char name[16];                      /* Name (for debugging purposes). */
  uint8_t *stack;                     /* Saved stack pointer. */
  int priority;                       /* Priority. */

  int64_t start;                      /* Start sleeping time */
  int64_t ticks;                      /* Total sleeping time */

  struct list_elem allelem;           /* List element for all threads list */
  /* Shared between thread.c and synch.c. */
  struct list_elem elem;

  //priority donation
  int donee_prio;
  int old_priority;
  int lock_num;
  struct thread *donee;
  struct list *waiters;

  // for mlfqs, fixed point format
  int nice;
  int recent_cpu;

#ifdef USERPROG
  /* Owned by userprog/process.c. */
  uint32_t *pagedir;                  /* Page directory. */
  char file_name[NAME_LENGTH];
  int fd;                             /* File descriptor */
  int exit_status;                      
  int load_status;
  struct file *open_file[OPEN_MAX];   /* Opened file list */
  struct hash children;               /* Children processes */
  struct thread *parent;              /* Parent process */
  struct hash_elem hash_elem;           /* Hash table elem */
#endif

  /* Owned by thread.c. */
  unsigned magic;                     /* Detects stack overflow. */
};

/* If false (default), use round-robin scheduler.
   If true, use multi-level feedback queue scheduler.
   Controlled by kernel command-line option "-o mlfqs". */
extern bool thread_mlfqs;

void thread_init (void);
void thread_start (void);

void thread_tick (void);
void thread_print_stats (void);

typedef void thread_func (void *aux);
tid_t thread_create (const char *name, int priority, thread_func *, void *);

void thread_block (void);
void thread_unblock (struct thread *);

struct thread *thread_current (void);
tid_t thread_tid (void);
const char *thread_name (void);

void thread_exit (void) NO_RETURN;
void thread_yield (void);

void thread_set_priority_all(void);
int thread_get_priority (void);
void thread_set_priority (int);
int thread_calculate_priority(struct thread *t);

int thread_get_nice (void);
void thread_set_nice (int);
void thread_set_recent_cpu(struct thread *);
void thread_set_recent_cpu_all(void);
int thread_get_recent_cpu (void);
void load_avg_update(void);
int thread_get_load_avg (void);


bool thread_greater_priority(const struct list_elem *a, const struct list_elem *b, void *aux UNUSED);

/* Prototypes for hash functions */
unsigned thread_hash(const struct hash_elem *child_elem, void *aux UNUSED);
bool thread_less(const struct hash_elem *a_elem, const struct hash_elem *b_elem, void *aux UNUSED);


/* List of all threads */
struct list all_list;

/* List of processes in THREAD_READY state, that is, processes
   that are ready to run but not actually running. */
struct list ready_list;

/* List of sleeping threads */
struct list sleep_list;

int load_avg; // fixed point format

#endif /* threads/thread.h */
