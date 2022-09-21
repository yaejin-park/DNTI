package com.a601.backend.api.service;

import com.a601.backend.api.domain.dto.request.BoardLikeRequest;
import com.a601.backend.api.domain.dto.request.BoardRequest;
import com.a601.backend.api.domain.dto.request.UserRequest;
import com.a601.backend.api.domain.dto.response.BoardResponse;
import com.a601.backend.api.domain.entity.Board;
import com.a601.backend.api.domain.entity.BoardLike;
import com.a601.backend.api.domain.entity.User;
import com.a601.backend.api.domain.enums.ErrorCode;
import com.a601.backend.api.exception.CustomException;
import com.a601.backend.api.repository.BoardLikeRepository;
import com.a601.backend.api.repository.BoardRepository;
import com.a601.backend.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    private final BoardLikeRepository boardLikeRepository;


    @Override
    public BoardResponse findByBoardId(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
        BoardResponse boardResponse = new BoardResponse(board);

        return boardResponse;
    }

    @Override
    public Page<BoardResponse> findAllByOrderByCreatedTime(Pageable pageable) {
        return boardRepository.findAllByOrderByCreatedTimeDesc(pageable).map(BoardResponse::new);
    }



    @Override
    public Long writeBoard(BoardRequest request) {
        User user = userRepository.getReferenceById(request.getUserId());
        Board board = Board.builder()
                .user(user)
                .title(request.getTitle())
                .contents(request.getContents()).build();
        boardRepository.save(board);
        return board.getBoardId();
    }

    @Override
    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
        if(board != null){
            boardRepository.deleteById(boardId);
        }
    }

    @Override
    @Transactional
    public void modifyBoard(BoardRequest request, Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
        board.modify(request.getTitle(), request.getContents());
    }

    @Override
    @Transactional
    public void updateHit(Long boardId) {

        boardRepository.findById(boardId).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND)).increaseHit();

    }

    @Override
    public Page<BoardResponse> searchBoard(Long category, String keyword, Pageable pageable) {
        // 제목으로 검색
        if(category== 0){
            return boardRepository.findByTitleContainingOrderByCreatedTimeDesc(keyword, pageable).map(BoardResponse::new);
        } 
        // 내용으로 검색
        else if(category ==1){
            return boardRepository.findByContentsContainingOrderByCreatedTimeDesc(keyword, pageable).map(BoardResponse::new);
        }
        return null;
    }

    @Override
    @Transactional
    public void addBoardLike(BoardLikeRequest boardLikeRequest) {
        Board board = boardRepository.findById(boardLikeRequest.getBoardId()).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
        User user = userRepository.findById(boardLikeRequest.getUserId()).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
        // 좋아요 1 증가
        board.addBoardLike();
        BoardLike boardLike = BoardLike.builder()
                .user(user)
                .board(board)
                .build();
        
        // 좋아요 정보 저장
        boardLikeRepository.save(boardLike);

    }

    @Override
    @Transactional
    public void cancelBoardLike(BoardLikeRequest boardLikeRequest) {
        Board board = boardRepository.findById(boardLikeRequest.getBoardId()).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
        board.cancelBoardLike();

        BoardLike boardLike = boardLikeRepository.findByEmailAndBoardId(boardLikeRequest.getUserId(), boardLikeRequest.getBoardId());
        if(boardLike != null){
            boardLikeRepository.delete(boardLike);
        }
    }

    @Override
    public List<BoardResponse> getMyBoard(String email) {
        List<BoardResponse> myList = boardRepository.findMyBoard(email)
                .stream()
                .map(board -> BoardResponse.builder()
                        .boardId(board.getBoardId())
                        .userId(board.getUser().getEmail())
                        .title(board.getTitle())
                        .contents(board.getContents())
                        .hit(board.getHit())
                        .boardLike(board.getBoardLike())
                        .commentCount(board.getReplyList().size())
                        .createdTime(board.getCreatedTime())
                        .modifiedTime(board.getModifiedTime())
                        .build()).collect(Collectors.toList());

        return myList;
    }


}
