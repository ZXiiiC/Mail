DATAS SEGMENT
    file1 db 'D:\ks1.txt',00
    handle1 dw 0
    buf db 256 dup(0)
    op_er db 'Open file error!$';打开文件失败
    rd_er db 'Read file error!$'
    cls_er db 'Close file error!$'
DATAS ENDS
STACKS SEGMENT
    dw 20 dup(0)
STACKS ENDS
CODES SEGMENT
    ASSUME DS:DATAS,SS:STACKS,CS:CODES
START:
    mov ax,DATAS
    mov ds,ax
    mov ax,STACKS
    mov ss,ax
    lea dx,file1
    mov ax,3d00h
    int 21H
    jnc con1 ;打开成功，程序继续
    push dx
    lea dx,op_er
    call prt
    pop dx
    jmp ed
  con1:
    mov handle1,ax
    mov bx,ax
    mov cx,255
    lea dx,buf
    mov ah,3fh
    int 21H
    jnc con2
    push dx
    lea dx,rd_er
    call prt
    pop dx
    jmp ed
  con2:
    mov bx,handle1
    mov ah,3eh
    int 21H
    jnc ed
    push dx
    lea dx,cls_er
    call prt
    pop dx
    jmp ed
  ed:
    MOV AH,4CH
    INT 21H
prt proc
    push ax
    mov ah,09h
    int 21H
    pop ax
    ret
prt endp
CODES ENDS
END start
