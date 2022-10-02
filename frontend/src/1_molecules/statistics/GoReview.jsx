import axios from "axios";
import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import styles from "./GoReview.module.css";


function GoReview({dong}) {
  const [reviews, setReviews] = useState()
  const [rCheck, setRCheck] = useState(false)
  const [boards, setBoards] = useState()

  async function getReview() {
    await axios(`http://j7a601.p.ssafy.io:9090/api/review/hot?dong=${dong}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then(function(res) {
        setReviews(res.data.response);
        console.log("data", reviews);
        
      })
      .catch(error => {
        console.error("실패:", error);
      });
  }
  
  useEffect(() => {
    getReview()
  }, [dong]);

  useEffect(() => {
    if (reviews) { 
      setRCheck(true);
    
    }
  }, [reviews]);

  return (
    <div className={styles.page}>
      <div className={styles.container}>
        <div className={styles.review}>
          <div className={styles.titleArea}>
            <div className={styles.title}>{dong} 리뷰</div>
            <Link to={`../board/post`}>
              <button className={styles.moreBtn}>더보기</button>            
            </Link>
          </div>
          {rCheck? (
          <div className={styles.contentArea}>
            {reviews[0] && (
              <div className={styles.contentLine}>
                <div>{reviews[0].title}</div>
                <div>{reviews[0].score}</div>
              </div>
            )}
            {reviews[1] && (
              <div className={styles.contentLine}>
                <div>{reviews[1].title}</div>
                <div>{reviews[1].score}</div>
              </div>
            )}
            {reviews[2] && (
              <div className={styles.contentLine}>
                <div>{reviews[2].title}</div>
                <div>{reviews[2].score}</div>
              </div>
            )}
          </div>            
          ) : (
            <div>loading</div>
          )}

        </div>

        <div className={styles.board}>
          <div className={styles.title}>{dong} 게시글</div>
          <Link to={`../board/review`}>
            <button className={styles.moreBtn}>더보기</button>            
          </Link>
        </div>
      </div>
    </div>
  );
}
export default GoReview
